package nl.ordina.kijkdoos.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 15-2-2017.
 */

public class BluetoothServiceTest {

    private BluetoothService bluetoothService;
    private DeviceFoundListener mockedCallback;

    @Before
    public void setUp() throws Exception {
        Context mockedContext = mock(Context.class);
        BluetoothManager mockedBluetoothManager = mock(BluetoothManager.class);
        BluetoothAdapter mockedBluetoothAdapter = mock(BluetoothAdapter.class);

        when(mockedContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(mockedBluetoothManager);
        when(mockedBluetoothManager.getAdapter()).thenReturn(mockedBluetoothAdapter);

        bluetoothService = new BluetoothService(mockedContext);
        mockedCallback = mock(DeviceFoundListener.class);
    }

    @Test
    public void searchDeviceShouldTriggerTheCallback() throws Exception {
        bluetoothService.searchDevices(mockedCallback);

        verify(mockedCallback).onDeviceFound();
    }

    @Test(expected = AssertionError.class)
    public void testNullCallback() throws Exception {
        bluetoothService.searchDevices(null);
    }
}
