package nl.ordina.kijkdoos.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController.UUID.SERVICE;

/**
 * Created by coenhoutman on 15-2-2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BluetoothDiscoveryService extends AbstractBluetoothService {

    private final BluetoothLeScanner bluetoothScanner;
    private final ScanFilter scanFilter;
    private ScanCallback scanCallback;

    public BluetoothDiscoveryService(Context context) {
        super(context);
        bluetoothScanner = getBluetoothAdapter().getBluetoothLeScanner();

        scanFilter = new ScanFilter.Builder().setServiceUuid(SERVICE.getUuid()).build();
    }

    @Override
    public void searchDevices(@NonNull DeviceFoundListener callback) {
        assertNotNull(callback);
        if (scanCallback != null) {
            return;
        }

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                callback.onDeviceFound(new ViewBoxRemoteController(result.getDevice()));
            }
        };
        bluetoothScanner.startScan(Collections.singletonList(scanFilter), new ScanSettings.Builder().build(), scanCallback);
    }

    @Override
    public void stopSearch() {
        bluetoothScanner.stopScan(scanCallback);
    }
}
