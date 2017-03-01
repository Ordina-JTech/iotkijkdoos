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
import android.support.annotation.VisibleForTesting;

import java.util.Collections;

import static junit.framework.Assert.assertNotNull;
import static nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController.UUID.SERVICE;

/**
 * Created by coenhoutman on 15-2-2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BluetoothDiscoveryService extends AbstractBluetoothService {

    private final BluetoothLeScanner bluetoothScanner;

    private ScanFilter scanFilter;
    private ScanSettings scanSettings;
    private ScanCallback scanCallback;

    public BluetoothDiscoveryService(Context context) {
        this(context, new ScanFilter.Builder().setServiceUuid(new ParcelUuid(SERVICE.getUuid())).build(),
                new ScanSettings.Builder().build());
    }

    @VisibleForTesting
    BluetoothDiscoveryService(Context context, ScanFilter scanFilter, ScanSettings scanSettings) {
        super(context);

        this.scanFilter = scanFilter;
        this.scanSettings = scanSettings;
        bluetoothScanner = getBluetoothAdapter().getBluetoothLeScanner();
    }

    @Override
    public void searchDevices(@NonNull DeviceFoundListener callback) {
        assertNotNull(callback);

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                callback.onDeviceFound(new ViewBoxRemoteController(result.getDevice()));
            }
        };
        bluetoothScanner.startScan(Collections.singletonList(scanFilter), scanSettings, scanCallback);
    }

    @Override
    public void stopSearch() {
        if (scanCallback == null) {
            return;
        }

        bluetoothScanner.stopScan(scanCallback);
    }
}
