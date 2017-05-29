package nl.ordina.kijkdoos.services;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteControllerService;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 18/04/2017.
 */
public class ViewBoxRemoteControllerServiceTest {

    @Rule
    public ServiceTestRule serviceRule = new ServiceTestRule();
    private ViewBoxRemoteControllerService viewBoxRemoteControllerService;

    @Before
    public void startService() throws Exception {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), ViewBoxRemoteControllerService.class);
        serviceRule.startService(intent);

        final IBinder binder = serviceRule.bindService(intent);
        viewBoxRemoteControllerService = ((ViewBoxRemoteControllerService.LocalBinder) binder).getService();
    }

    @Test
    public void testConnectViewBoxRemoteControllerService() throws Exception {
        assertNotNull(viewBoxRemoteControllerService);
    }

    @Test
    public void testViewBoxIsResetWhenDisconnecting() throws Exception {
        final ViewBoxRemoteController viewBoxRemoteController = mock(ViewBoxRemoteController.class);
        when(viewBoxRemoteController.isConnected()).thenReturn(true);

        viewBoxRemoteControllerService.connect(viewBoxRemoteController, null, null);
        viewBoxRemoteControllerService.disconnect();

        verify(viewBoxRemoteController).reset(any());
    }
}