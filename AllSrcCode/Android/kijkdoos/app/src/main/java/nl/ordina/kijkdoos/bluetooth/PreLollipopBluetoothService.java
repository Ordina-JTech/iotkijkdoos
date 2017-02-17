package nl.ordina.kijkdoos.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Created by coenhoutman on 16-2-2017.
 */
public class PreLollipopBluetoothService extends AbstractBluetoothService {
    public PreLollipopBluetoothService(Context context) {
        super(context);
    }

    @Override
    public void searchDevices(@NonNull DeviceFoundListener callback) {
        getBluetoothAdapter().startLeScan((device, rssi, scanRecord) -> {
            callback.onDeviceFound(new BluetoothDeviceWrapper(device));
        });
    }
}
