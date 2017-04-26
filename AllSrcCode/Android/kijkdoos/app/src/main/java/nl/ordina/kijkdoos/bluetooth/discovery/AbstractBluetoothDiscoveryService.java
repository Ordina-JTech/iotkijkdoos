package nl.ordina.kijkdoos.bluetooth.discovery;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Getter;

/**
 * Created by coenhoutman on 15-2-2017.
 */
public abstract class AbstractBluetoothDiscoveryService {
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @Getter
    private final BluetoothManager bluetoothManager;

    @Getter
    @Nullable
    private final BluetoothAdapter bluetoothAdapter;

    public AbstractBluetoothDiscoveryService(Context context) {
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public abstract void searchDevices(@NonNull DeviceFoundListener callback);
    public abstract void stopSearch();

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static void askUserToEnableBluetooth(Activity activity) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
    }
}
