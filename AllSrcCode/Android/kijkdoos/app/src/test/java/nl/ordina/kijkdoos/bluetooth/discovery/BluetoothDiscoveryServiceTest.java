package nl.ordina.kijkdoos.bluetooth.discovery;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.bluetooth.discovery.BluetoothDiscoveryService;
import nl.ordina.kijkdoos.bluetooth.discovery.DeviceFoundListener;

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
public class BluetoothDiscoveryServiceTest {

    private BluetoothDiscoveryService bluetoothDiscoveryService;
    private BluetoothDevice mockedBluetoothDevice;

    @Before
    public void setUp() throws Exception {
        Context mockedContext = mock(Context.class);
        BluetoothManager mockedBluetoothManager = mock(BluetoothManager.class);
        BluetoothAdapter mockedBluetoothAdapter = mock(BluetoothAdapter.class);

        when(mockedContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(mockedBluetoothManager);
        when(mockedBluetoothManager.getAdapter()).thenReturn(mockedBluetoothAdapter);

        BluetoothLeScanner mockedBluetoothScanner = mock(BluetoothLeScanner.class);
        when(mockedBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mockedBluetoothScanner);

        mockedBluetoothDevice = mock(BluetoothDevice.class);

        ScanResult mockedScanResult = mock(ScanResult.class);
        when(mockedScanResult.getDevice()).thenReturn(mockedBluetoothDevice);

        doAnswer(invocationOnMock -> {
            invocationOnMock.getArgumentAt(2, ScanCallback.class).onScanResult(0, mockedScanResult);
            return null;
        }).when(mockedBluetoothScanner).startScan(any(List.class), any(ScanSettings.class), any(ScanCallback.class));

        bluetoothDiscoveryService = new BluetoothDiscoveryService(mockedContext,
                mock(ScanFilter.class), mock(ScanSettings.class));
    }

    @Test
    public void searchDeviceShouldTriggerTheCallback() throws Exception {
        DeviceFoundListener mockedCallback = mock(DeviceFoundListener.class);
        bluetoothDiscoveryService.searchDevices(mockedCallback);

        ArgumentCaptor<ViewBoxRemoteController> argumentCaptor = ArgumentCaptor.forClass(ViewBoxRemoteController.class);

        verify(mockedCallback).onDeviceFound(argumentCaptor.capture());
        assertEquals(mockedBluetoothDevice, argumentCaptor.getValue().getDevice());
    }

    @Test(expected = AssertionError.class)
    public void testNullCallback() throws Exception {
        bluetoothDiscoveryService.searchDevices(null);
    }
}
