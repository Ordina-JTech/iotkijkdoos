package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by coenhoutman on 16-2-2017.
 */
public class PreLollipopBluetoothService extends AbstractBluetoothService {

    private BluetoothAdapter.LeScanCallback scanCallback;

    public PreLollipopBluetoothService(Context context) {
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
        getBluetoothAdapter().startLeScan(scanCallback);
    }

    @Override
    public void stopSearch() {
        getBluetoothAdapter().stopLeScan(scanCallback);
        scanCallback = null;
    }
}
