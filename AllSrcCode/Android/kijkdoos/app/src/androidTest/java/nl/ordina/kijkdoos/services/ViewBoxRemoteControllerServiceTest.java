package nl.ordina.kijkdoos.services;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by coenhoutman on 18/04/2017.
 */
public class ViewBoxRemoteControllerServiceTest {

    @Rule
    public ServiceTestRule serviceRule = new ServiceTestRule();

    @Test
    public void testConnectViewBoxRemoteController() throws Exception {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), ViewBoxRemoteControllerService.class);
        serviceRule.startService(intent);
        final IBinder binder = serviceRule.bindService(intent);
        ViewBoxRemoteControllerService viewBoxRemoteControllerService = ((ViewBoxRemoteControllerService.LocalBinder) binder).getService();
        assertNotNull(viewBoxRemoteControllerService);
    }
}