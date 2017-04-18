package nl.ordina.kijkdoos.view.search;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.BluetoothConnectionFragment;
import nl.ordina.kijkdoos.bluetooth.DeviceFoundListener;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.services.ViewBoxRemoteControllerService;
import nl.ordina.kijkdoos.view.BluetoothDisabledActivity;
import nl.ordina.kijkdoos.view.control.ControlViewBoxActivity;

import static android.bluetooth.BluetoothAdapter.STATE_TURNING_OFF;
import static nl.ordina.kijkdoos.R.string.FailedToConnect;
import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;
import static nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService.REQUEST_ENABLE_BLUETOOTH;
import static nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService.askUserToEnableBluetooth;

public class SearchViewBoxActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DeviceFoundListener {

    @Inject
    AbstractBluetoothService bluetoothService;

    @BindView(R.id.viewBoxList)
    ListView viewBoxList;

    @BindView(R.id.activity_search_view_box)
    SwipeRefreshLayout swipeRefreshLayout;

    private ViewBoxListAdapter viewBoxListAdapter;
    private Optional<MenuItem> refreshMenuItem;
    private ViewBoxRemoteControllerService viewBoxRemoteControllerService;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_box);
        ButterKnife.bind(this);
        getViewBoxApplication(this).getApplicationComponent().inject(this);
        refreshMenuItem = Optional.empty();

        createViewBoxList();
        createSwipeRefreshLayout();
        createBluetoothConnectionFragment();
        createViewBoxRemoteControllerService();
    }

    private void createViewBoxRemoteControllerService() {
        final Intent intent = new Intent(this, ViewBoxRemoteControllerService.class);
        startService(intent);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                viewBoxRemoteControllerService = ((ViewBoxRemoteControllerService.LocalBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                viewBoxRemoteControllerService = null;
            }
        };
    }

    private void createBluetoothConnectionFragment() {
        final BluetoothConnectionFragment bluetoothConnectionFragment = BluetoothConnectionFragment.add(this);
        bluetoothConnectionFragment.addConnectionEventHandler(state -> state == STATE_TURNING_OFF, state -> askUserToEnableBluetooth(this));
    }

    private void createSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this::searchViewBoxes);
    }

    private void createViewBoxList() {
        viewBoxListAdapter = new ViewBoxListAdapter(this);
        viewBoxList.setAdapter(viewBoxListAdapter);
        viewBoxList.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_viewbox_menu, menu);

        refreshMenuItem = Optional.of(menu.getItem(0));

        return true;
    }

    @Override
    protected void onResume() {
        handleAppPermissions();
        handleBluetooth();

        final Intent intent = new Intent(this, ViewBoxRemoteControllerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        super.onResume();
    }

    private void handleBluetooth() {
        if (bluetoothService.isBluetoothEnabled()) {
            programmaticSearch();
        } else {
            askUserToEnableBluetooth(this);
        }
    }

    private void programmaticSearch() {
        swipeRefreshLayout.setRefreshing(true);
        searchViewBoxes();
    }

    private void searchViewBoxes() {
        viewBoxListAdapter.clear();
        refreshMenuItem.executeIfPresent(menuItem -> menuItem.setEnabled(false));

        bluetoothService.searchDevices(this);
        swipeRefreshLayout.postDelayed(this::stopSearch, 5000);
    }

    private void stopSearch() {
        refreshMenuItem.executeIfPresent(menuItem -> menuItem.setEnabled(true));

        bluetoothService.stopSearch();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void handleAppPermissions() {
        final int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_CANCELED) {

                final Intent intent = new Intent(this, BluetoothDisabledActivity.class);
                startActivity(intent);

                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleBluetooth();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        bluetoothService.stopSearch();
        viewBoxListAdapter.clear();

        unbindService(serviceConnection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            if (!swipeRefreshLayout.isRefreshing()) {
                programmaticSearch();

                return true;
            }
        }

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        viewBoxListAdapter.disableItem(position);
        stopSearch();

        ViewBoxRemoteController viewBoxRemoteController = viewBoxListAdapter.getViewBoxRemoteController(position);
        final Consumer<Void> onConnected = (aVoid) -> runOnUiThread(() -> {
            Intent intent = new Intent(SearchViewBoxActivity.this, ControlViewBoxActivity.class);
            startActivity(intent);
        });

        final Consumer<Void> onConnectionError = (aVoid) -> runOnUiThread(() -> {
            Toast.makeText(SearchViewBoxActivity.this, FailedToConnect, Toast.LENGTH_SHORT).show();
            viewBoxListAdapter.removeViewBoxRemoteController(viewBoxRemoteController);
            viewBoxListAdapter.enableItems();
        });

        viewBoxRemoteControllerService.connect(viewBoxRemoteController, onConnected, onConnectionError);
    }

    @Override
    public void onDeviceFound(ViewBoxRemoteController bluetoothDevice) {
        runOnUiThread(() -> viewBoxListAdapter.addViewBoxRemoteController(bluetoothDevice));
    }
}
