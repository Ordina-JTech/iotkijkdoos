package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import lombok.Getter;

import static android.text.TextUtils.isEmpty;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by coenhoutman on 16-2-2017.
 */

public class ViewBox {

    @Getter
    private BluetoothDevice device;

    public ViewBox(@NonNull BluetoothDevice device) {
        assertNotNull(device);
        this.device = device;
    }

    public String getName() {
        String deviceName = device.getName();

        return isEmpty(deviceName) ? getAddress() : device.getName();
    }

    public String getAddress() {
        return device.getAddress();
    }

    public void connect(final Context context, final OnConnectedListener listener) {
        ((Runnable) () -> device.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    Log.w(ViewBox.class.getSimpleName(), this + ": failed to connect (" + status + ")");
                    return;
                }

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    if (listener != null) {
                        listener.onConnected(ViewBox.this);
                    }
                }
            }
        })).run();
    }

    @Override
    public boolean equals(Object obj) {
        return device.equals(obj);
    }

    @Override
    public String toString() {
        return "ViewBox{" +
                "name=" + getName() +
                '}';
    }

    public interface OnConnectedListener {
        void onConnected(ViewBox viewBox);
    }

}
