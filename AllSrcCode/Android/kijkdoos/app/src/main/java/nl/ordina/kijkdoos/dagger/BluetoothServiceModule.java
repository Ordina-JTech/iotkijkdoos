package nl.ordina.kijkdoos.dagger;

import android.content.Context;
import android.os.Build;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.ordina.kijkdoos.bluetooth.discovery.AbstractBluetoothDiscoveryService;
import nl.ordina.kijkdoos.bluetooth.discovery.BluetoothDiscoveryService;
import nl.ordina.kijkdoos.bluetooth.discovery.PreLollipopBluetoothDiscoveryService;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Module(includes = AndroidApplicationModule.class)
public class BluetoothServiceModule {

    @Singleton
    @Provides
    public AbstractBluetoothDiscoveryService provideBluetoothService(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new PreLollipopBluetoothDiscoveryService(context);
        }
        return new BluetoothDiscoveryService(context);
    }
}
