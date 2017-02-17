package nl.ordina.kijkdoos.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@TargetApi(21)
public class BluetoothServiceTest {

    private BluetoothManager mockedBluetoothManager;
    private BluetoothAdapter mockedBluetoothAdapter;
    private Context mockedContext;

    @Before
    public void setUp() throws Exception {
        mockedContext = mock(Context.class);
        mockedBluetoothManager = mock(BluetoothManager.class);
        mockedBluetoothAdapter = mock(BluetoothAdapter.class);

        when(mockedContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(mockedBluetoothManager);
        when(mockedBluetoothManager.getAdapter()).thenReturn(mockedBluetoothAdapter);
    }

    @Test
    public void searchDeviceShouldTriggerTheCallback() throws Exception {
        BluetoothLeScanner mockedBluetoothScanner = mock(BluetoothLeScanner.class);
        when(mockedBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mockedBluetoothScanner);
        BluetoothDevice mockedBluetoothDevice = mock(BluetoothDevice.class);
        ScanResult mockedScanResult = mock(ScanResult.class);
        when(mockedScanResult.getDevice()).thenReturn(mockedBluetoothDevice);

        doAnswer(invocationOnMock -> {
            invocationOnMock.getArgumentAt(0, ScanCallback.class).onScanResult(0, mockedScanResult);
            return null;
        }).when(mockedBluetoothScanner).startScan(any(ScanCallback.class));

        BluetoothService bluetoothService = new BluetoothService(mockedContext);
        DeviceFoundListener mockedCallback = mock(DeviceFoundListener.class);
        bluetoothService.searchDevices(mockedCallback);

        ArgumentCaptor<BluetoothDeviceWrapper> argumentCaptor = ArgumentCaptor.forClass(BluetoothDeviceWrapper.class);

        verify(mockedCallback).onDeviceFound(argumentCaptor.capture());

        assertEquals(mockedBluetoothDevice, argumentCaptor.getValue().getDevice());
    }

    @Test(expected = AssertionError.class)
    public void testNullCallback() throws Exception {
        BluetoothService bluetoothService = new BluetoothService(mockedContext);
        bluetoothService.searchDevices(null);
    }

    @Test
    public void testIsBluetoothSupportedReturnsFalse() throws Exception {
        when(mockedContext.has)
        BluetoothService bluetoothService = new BluetoothService(mockedContext);

    }
}
