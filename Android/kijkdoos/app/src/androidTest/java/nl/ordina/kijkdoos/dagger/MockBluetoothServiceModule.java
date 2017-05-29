package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.ordina.kijkdoos.bluetooth.discovery.AbstractBluetoothDiscoveryService;
import nl.ordina.kijkdoos.bluetooth.discovery.BluetoothDiscoveryService;

import static org.mockito.Mockito.mock;

/**
 * Created by coenhoutman on 15-2-2017.
 */
@Module
public class MockBluetoothServiceModule {

    @Singleton
    @Provides
    public AbstractBluetoothDiscoveryService provideBluetoothService() {
        return mock(BluetoothDiscoveryService.class);
    }
}
