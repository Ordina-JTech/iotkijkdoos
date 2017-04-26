package nl.ordina.kijkdoos.bluetooth.discovery;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

/**
 * Created by coenhoutman on 15-2-2017.
 */
public interface DeviceFoundListener {
    void onDeviceFound(ViewBoxRemoteController bluetoothDevice);
}
