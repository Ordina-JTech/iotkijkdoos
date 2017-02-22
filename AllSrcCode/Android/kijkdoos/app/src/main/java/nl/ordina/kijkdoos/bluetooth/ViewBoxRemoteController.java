package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.annimon.stream.Stream;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import lombok.Getter;
import nl.ordina.kijkdoos.dagger.BackgroundServiceFactory;
import nl.ordina.kijkdoos.threading.BackgroundService;

import static android.text.TextUtils.isEmpty;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by coenhoutman on 16-2-2017.
 */
@Parcel
public class ViewBoxRemoteController {

    public enum UUID {
        SERVICE("0000ffe0-0000-1000-8000-00805f9b34fb"), CHARACTERISTIC("00002a37-0000-1000-8000-00805f9b34fb");

        private final String uuid;

        UUID(String uuid) {
            this.uuid = uuid;
        }

        public ParcelUuid getUuid() {
            return new ParcelUuid(java.util.UUID.fromString(uuid));
        }
    }

    @Getter
    private BluetoothDevice device;

    @Transient
    private BluetoothGatt bluetoothGatt;

    @Inject
    @Transient
    BackgroundService backgroundService;

    @Transient
    private BluetoothCallbackRegister bluetoothCallbackRegister;

    @ParcelConstructor
    public ViewBoxRemoteController(@NonNull BluetoothDevice device) {
        assertNotNull(device);
        this.device = device;

        BackgroundServiceFactory.getComponent().inject(this);
        bluetoothCallbackRegister = new BluetoothCallbackRegister();
    }

    public String getName() {
        String deviceName = device.getName();

        return isEmpty(deviceName) ? getAddress() : device.getName();
    }

    public String getAddress() {
        return device.getAddress();
    }

    public void connect(final Context context, final OnConnectedListener listener) {
        bluetoothCallbackRegister.registerOnConnectedListener(listener);
        backgroundService.getExecutorService().submit(() -> device.connectGatt(context, false, bluetoothCallbackRegister));
    }

    public void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothCallbackRegister.registerOnDisconnectedListener(() -> {
                bluetoothGatt.close();
                bluetoothGatt = null;
            });
        }
    }

    public Parcelable wrapInParcelable() {
        return Parcels.wrap(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ViewBoxRemoteController)) {
            return false;
        }
        return device.equals(((ViewBoxRemoteController) obj).device);
    }

    @Override
    public String toString() {
        return "ViewBox{" +
                "name=" + getName() +
                '}';
    }

    public interface OnConnectedListener {
        void onConnected();
    }

    public interface OnDisconnectedListener {
        void onDisconnected();
    }

    private class BluetoothCallbackRegister extends BluetoothGattCallback {
        private Set<OnConnectedListener> onConnectedListeners = new HashSet<>();
        private Set<OnDisconnectedListener> onDisconnectedListeners = new HashSet<>();

        private void registerOnConnectedListener(OnConnectedListener listener) {
            onConnectedListeners.add(listener);
        }

        private void registerOnDisconnectedListener(OnDisconnectedListener listener) {
            onDisconnectedListeners.add(listener);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.w(ViewBoxRemoteController.class.getSimpleName(), this + ": failed to connect (" + status + ")");
                return;
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGatt = gatt;

                Stream.of(onConnectedListeners).forEach(OnConnectedListener::onConnected);
                onConnectedListeners.clear();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Stream.of(onDisconnectedListeners).forEach(OnDisconnectedListener::onDisconnected);
                onDisconnectedListeners.clear();
            }
        }
    }
}
