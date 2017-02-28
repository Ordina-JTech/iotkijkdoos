package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.annimon.stream.Stream;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;
import org.parceler.Transient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

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
        SERVICE("0000ffe0-0000-1000-8000-00805f9b34fb"), CHARACTERISTIC("0000ffe1-0000-1000-8000-00805f9b34fb");

        private final String uuid;

        UUID(String uuid) {
            this.uuid = uuid;
        }

        public java.util.UUID getUuid() {
            return java.util.UUID.fromString(uuid);
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

    @Transient
    private BluetoothGattService bluetoothGattService;

    @Transient
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;

    @VisibleForTesting
    protected ViewBoxRemoteController() {
    }

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

    public Future<Void> connect(final Context context) {
        return backgroundService.getExecutorService()
                .submit(() -> {
                    device.connectGatt(context, false, bluetoothCallbackRegister);
                    while ((bluetoothGattService == null || bluetoothGattCharacteristic == null) && !Thread.interrupted()) {
                    }

                    return null;
                });
    }

    public void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    public void toggleLeftLamp() {
        bluetoothGattCharacteristic.setValue("a");
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    public void toggleRightLamp() {
        bluetoothGattCharacteristic.setValue("b");
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
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

                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Stream.of(onDisconnectedListeners).filter(value -> value != null).forEach(OnDisconnectedListener::onDisconnected);
                onDisconnectedListeners.clear();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            bluetoothGattService = bluetoothGatt.getService(UUID.SERVICE.getUuid());
            bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(UUID.CHARACTERISTIC.getUuid());

            bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        }
    }
}
