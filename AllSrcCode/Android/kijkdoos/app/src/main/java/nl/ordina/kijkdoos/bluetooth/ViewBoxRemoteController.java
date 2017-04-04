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
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.annimon.stream.function.Consumer;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;
import org.parceler.Transient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    private Consumer<Void> connectConsumer;

    @Transient
    @Nullable
    @Getter
    @Setter
    private Consumer<Void> disconnectConsumer;

    @Transient
    private Map<String, Consumer<Void>> messageResponseListeners;

    @VisibleForTesting
    protected ViewBoxRemoteController() {
    }

    @ParcelConstructor
    public ViewBoxRemoteController(@NonNull BluetoothDevice device) {
        assertNotNull(device);
        this.device = device;

        BackgroundServiceFactory.getComponent().inject(this);
        bluetoothCallbackRegister = new BluetoothCallbackRegister();
        messageResponseListeners = new HashMap<>();
    }

    public String getName() {
        String deviceName = device.getName();

        return isEmpty(deviceName) ? getAddress() : device.getName();
    }

    public String getAddress() {
        return device.getAddress();
    }

    public Future<Void> connect(final Context context) {
        return connect(context, null);
    }

    public Future<Void> connect(final Context context, Consumer<Void> onConnectConsumer) {
        connectConsumer = onConnectConsumer;

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
            reset(aVoid -> bluetoothGatt.disconnect());
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

    public void showGradient() {
        sendMessage("h");
    }

    public void specialEffect() {
        sendMessage("i");
    }

    public void reset(Consumer<Void> resetFinishedConsumer) {
        sendMessage("r");
        addMessageResponseListener("y", resetFinishedConsumer);
    }

    public Parcelable wrapInParcelable() {
        return Parcels.wrap(this);
    }

    private void sendMessage(String message) {
        if (bluetoothGatt == null) return;

        bluetoothGattCharacteristic.setValue(message);
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    private void addMessageResponseListener(String expectedMessage, Consumer<Void> messageResponseConsumer) {
        messageResponseListeners.put(expectedMessage, messageResponseConsumer);
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

            if (connectConsumer != null) {
                connectConsumer.accept(null);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            final byte[] value = characteristic.getValue();
            final byte[] valueWithoutLineEnding = Arrays.copyOf(value, value.length - 2);

            final String valueWithoutLineEndingString = new String(valueWithoutLineEnding);
            final Consumer<Void> consumer = messageResponseListeners.get(valueWithoutLineEndingString);
            if (consumer != null) {
                consumer.accept(null);
            }
        }
    }
}
