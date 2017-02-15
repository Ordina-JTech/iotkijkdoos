package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.ordina.kijkdoos.bluetooth.BluetoothService;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Module
public class BluetoothServiceModule {

    @Singleton
    @Provides
    public BluetoothService provideBluetoothService() {
        return new BluetoothService();
    }
}
