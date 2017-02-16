package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import lombok.Getter;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by coenhoutman on 16-2-2017.
 */

public class BluetoothDeviceWrapper {
    private String name;

    @Getter
    private BluetoothDevice device;

    public BluetoothDeviceWrapper(@NonNull BluetoothDevice device) {
        assertNotNull(device);
        this.device = device;
    }

    public String getName() {
        return device.getName();
    }

    @Override
    public boolean equals(Object obj) {
        return device.equals(obj);
    }
}
