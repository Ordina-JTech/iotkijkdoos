package nl.ordina.kijkdoos.bluetooth.discovery;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

/**
 * Created by coenhoutman on 16-2-2017.
 */
public class PreLollipopBluetoothDiscoveryService extends AbstractBluetoothDiscoveryService {

    private BluetoothAdapter.LeScanCallback scanCallback;

    public PreLollipopBluetoothDiscoveryService(Context context) {
        super(context);
    }

    @Override
    public void searchDevices(@NonNull DeviceFoundListener callback) {
        if (scanCallback != null) {
            return;
        }

        scanCallback = (device, rssi, scanRecord) -> {
            callback.onDeviceFound(new ViewBoxRemoteController(device));
        };

        if (getBluetoothAdapter() != null) {
            getBluetoothAdapter().startLeScan(scanCallback);
        }
    }

    @Override
    public void stopSearch() {
        if (getBluetoothAdapter() != null) {
            getBluetoothAdapter().stopLeScan(scanCallback);
            scanCallback = null;
        }
    }
}
