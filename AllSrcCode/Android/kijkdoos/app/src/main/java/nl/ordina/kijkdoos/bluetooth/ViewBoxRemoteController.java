package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;
import org.parceler.Transient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import nl.ordina.kijkdoos.dagger.BackgroundServiceFactory;
import nl.ordina.kijkdoos.threading.BackgroundService;
import nl.ordina.kijkdoos.view.control.ControlDiscoBallFragment;
import nl.ordina.kijkdoos.view.control.speaker.ControlSpeakerFragment;

import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTING;
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

    @Transient
    @Nullable
    @Getter
    @Setter
    private Consumer<Void> disconnectConsumer;

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

    public void switchLeftLamp(boolean status) {
        sendMessage("a" + parseBoolean(status));
    }


    public void toggleRightLamp(boolean status) {
        sendMessage("b" + parseBoolean(status));
    }

    private int parseBoolean(boolean value) {
        return value ? 1 : 0;
    }

    public void setDiscoBallColor(ControlDiscoBallFragment.DiscoBallColor color) {
        sendMessage(color.getMessage());
    }

    public void switchOffDiscoBall() {
        sendMessage("c0");
    }

    public void playSong(ControlSpeakerFragment.Song song) {
        sendMessage(song.getMessage());
    }

    public void rotateTelevision(int degree) {
        if (degree < 0 || degree > 179) {
            throw new IllegalArgumentException("Accepted values are between 0 and 179");
        }

        sendMessage("g" + degree + "\n");
    }

    public Parcelable wrapInParcelable() {
        return Parcels.wrap(this);
    }

    private void sendMessage(String value) {
        bluetoothGattCharacteristic.setValue(value);
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
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

    private class BluetoothCallbackRegister extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == STATE_DISCONNECTED || newState == STATE_DISCONNECTING) {
                bluetoothGatt.close();

                if (disconnectConsumer != null) {
                    disconnectConsumer.accept(null);
                }
            }

            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.w(ViewBoxRemoteController.class.getSimpleName(), this + ": failed to connect (" + status + ")");

                return;
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGatt = gatt;

                bluetoothGatt.discoverServices();
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
