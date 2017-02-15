package nl.ordina.kijkdoos;

import android.app.Application;

import lombok.Getter;
import nl.ordina.kijkdoos.dagger.ApplicationComponent;
import nl.ordina.kijkdoos.dagger.BluetoothServiceModule;
import nl.ordina.kijkdoos.dagger.DaggerApplicationComponent;

/**
 * Created by coenhoutman on 15-2-2017.
 */

public class ViewBoxApplication extends Application {

    @Getter
    private final ApplicationComponent applicationComponent = createApplicationComponent();

    protected ApplicationComponent createApplicationComponent() {
        return DaggerApplicationComponent
                .builder()
                .bluetoothServiceModule(new BluetoothServiceModule())
                .build();
    }
}
