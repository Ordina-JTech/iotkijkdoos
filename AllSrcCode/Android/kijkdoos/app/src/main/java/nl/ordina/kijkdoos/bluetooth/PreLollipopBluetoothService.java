package nl.ordina.kijkdoos.bluetooth;

import android.content.Context;
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
            callback.onDeviceFound(new ViewBoxRemoteController(device));
        });
    }
}
