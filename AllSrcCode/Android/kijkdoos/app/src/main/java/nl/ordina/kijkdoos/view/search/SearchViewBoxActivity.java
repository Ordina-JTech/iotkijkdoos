package nl.ordina.kijkdoos.view.search;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.BluetoothConnectionFragment;
import nl.ordina.kijkdoos.view.BluetoothDisabledActivity;
import nl.ordina.kijkdoos.view.control.ControlViewBoxActivity;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.bluetooth.DeviceFoundListener;

import static android.bluetooth.BluetoothAdapter.STATE_ON;
import static android.bluetooth.BluetoothAdapter.STATE_TURNING_OFF;
import static nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService.REQUEST_ENABLE_BLUETOOTH;
import static nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService.askUserToEnableBluetooth;
import static nl.ordina.kijkdoos.view.control.ControlViewBoxActivity.EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER;
import static nl.ordina.kijkdoos.view.control.ControlViewBoxActivity.EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER;
import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;

public class SearchViewBoxActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DeviceFoundListener {

    @Inject
    AbstractBluetoothService bluetoothService;

    @BindView(R.id.viewBoxList)
    ListView viewBoxList;

    private ViewBoxListAdapter viewBoxListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_box);
        ButterKnife.bind(this);
        getViewBoxApplication(this).getApplicationComponent().inject(this);

        viewBoxListAdapter = new ViewBoxListAdapter(this);
        viewBoxList.setAdapter(viewBoxListAdapter);
        viewBoxList.setOnItemClickListener(this);

        final BluetoothConnectionFragment bluetoothConnectionFragment = BluetoothConnectionFragment.add(this);

        bluetoothConnectionFragment.addConnectionEventHandler(state -> state == STATE_TURNING_OFF, state -> askUserToEnableBluetooth(this));
    }

    @Override
    protected void onResume() {
        handleAppPermissions();
        handleBluetooth();

        super.onResume();
    }

    private void handleBluetooth() {
        if (bluetoothService.isBluetoothEnabled()) {
            bluetoothService.searchDevices(this);
        } else {
            askUserToEnableBluetooth(this);
        }
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bluetoothService.stopSearch();
        ViewBoxRemoteController viewBoxRemoteController = viewBoxListAdapter.getViewBoxRemoteController(position);
        Bundle bundledToAvoidSamsungBug = new Bundle();
        bundledToAvoidSamsungBug.putParcelable(EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER, viewBoxRemoteController.wrapInParcelable());

        final Future<Void> connect = viewBoxRemoteController.connect(this);
        new Thread() {
            @Override
            public void run() {
                try {
                    connect.get(5, TimeUnit.SECONDS);
                    viewBoxRemoteController.disconnect();
                    runOnUiThread(() -> {
                        Intent intent = new Intent(SearchViewBoxActivity.this, ControlViewBoxActivity.class);
                        intent.putExtra(EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER, bundledToAvoidSamsungBug);

                        startActivity(intent);
                    });
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onDeviceFound(ViewBoxRemoteController bluetoothDevice) {
        runOnUiThread(() -> viewBoxListAdapter.addViewBoxRemoteController(bluetoothDevice));
    }
}
