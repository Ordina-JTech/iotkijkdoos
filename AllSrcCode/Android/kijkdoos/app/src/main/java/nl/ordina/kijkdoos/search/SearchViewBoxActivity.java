package nl.ordina.kijkdoos.search;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
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
import nl.ordina.kijkdoos.ViewBoxActivity;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.bluetooth.DeviceFoundListener;

import static nl.ordina.kijkdoos.ViewBoxActivity.EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER;
import static nl.ordina.kijkdoos.ViewBoxActivity.EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER;
import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;

public class SearchViewBoxActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DeviceFoundListener {
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @Inject
    AbstractBluetoothService bluetoothService;

    @BindView(R.id.viewBoxList)
    ListView viewBoxList;

    private ViewBoxListAdapter viewBoxListAdapter;

    private boolean waitingForBluetoothBeingEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_box);
        ButterKnife.bind(this);
        getViewBoxApplication(this).getApplicationComponent().inject(this);

        viewBoxListAdapter = new ViewBoxListAdapter(this);
        viewBoxList.setAdapter(viewBoxListAdapter);
        viewBoxList.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        if (bluetoothService.isBluetoothEnabled()) {
            bluetoothService.searchDevices(this);
        } else if (!waitingForBluetoothBeingEnabled) {
            askUserToEnableBluetooth();
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            waitingForBluetoothBeingEnabled = false;
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.BluetoothNeededMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        bluetoothService.stopSearch();
        viewBoxListAdapter.clear();
    }

    private void askUserToEnableBluetooth() {
        waitingForBluetoothBeingEnabled = true;

        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
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
                        Intent intent = new Intent(SearchViewBoxActivity.this, ViewBoxActivity.class);
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
