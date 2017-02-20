package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import lombok.Getter;

import static android.text.TextUtils.isEmpty;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by coenhoutman on 16-2-2017.
 */

public class BluetoothDeviceWrapper {

    @Getter
    private BluetoothDevice device;

    public BluetoothDeviceWrapper(@NonNull BluetoothDevice device) {
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

    @Override
    public boolean equals(Object obj) {
        return device.equals(obj);
    }
}
