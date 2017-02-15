package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.annotation.NonNull;

import junit.framework.Assert;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by coenhoutman on 15-2-2017.
 */
public class BluetoothService {

    private final BluetoothManager bluetoothManager;
    private final BluetoothAdapter adapter;

    public BluetoothService(Context context) {
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
    }

    public void searchDevices(@NonNull DeviceFoundListener callback) {
        assertNotNull(callback);
        callback.onDeviceFound();
    }
}
