package nl.ordina.kijkdoos.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import lombok.Getter;

/**
 * Created by coenhoutman on 15-2-2017.
 */
public abstract class AbstractBluetoothService {
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @Getter
    private final BluetoothManager bluetoothManager;

    @Getter
    private final BluetoothAdapter bluetoothAdapter;

    public AbstractBluetoothService(Context context) {
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public abstract void searchDevices(@NonNull DeviceFoundListener callback);
    public abstract void stopSearch();

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public static void askUserToEnableBluetooth(Activity activity) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
    }
}
