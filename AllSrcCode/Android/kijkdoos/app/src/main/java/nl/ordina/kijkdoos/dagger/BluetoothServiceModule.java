package nl.ordina.kijkdoos.dagger;

import android.content.Context;
import android.os.Build;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.BluetoothService;
import nl.ordina.kijkdoos.bluetooth.PreLollipopBluetoothService;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Module(includes = AndroidApplicationModule.class)
public class BluetoothServiceModule {

    @Singleton
    @Provides
    public AbstractBluetoothService provideBluetoothService(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new PreLollipopBluetoothService(context);
        }
        return new BluetoothService(context);
    }
}
