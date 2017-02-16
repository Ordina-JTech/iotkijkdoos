package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by coenhoutman on 15-2-2017.
 */
public interface DeviceFoundListener {
    void onDeviceFound(BluetoothDeviceWrapper bluetoothDevice);
}
