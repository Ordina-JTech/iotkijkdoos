package nl.ordina.kijkdoos.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.support.annotation.NonNull;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by coenhoutman on 15-2-2017.
 */
@TargetApi(21)
public class BluetoothService extends AbstractBluetoothService {

    private final BluetoothLeScanner bluetoothScanner;

    public BluetoothService(Context context) {
        super(context);
        bluetoothScanner = getBluetoothAdapter().getBluetoothLeScanner();
    }

    @Override
    public void searchDevices(@NonNull DeviceFoundListener callback) {
        assertNotNull(callback);

        bluetoothScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                callback.onDeviceFound(result.getDevice());
            }
        });
    }

}
