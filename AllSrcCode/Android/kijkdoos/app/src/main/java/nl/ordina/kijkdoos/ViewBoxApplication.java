package nl.ordina.kijkdoos;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import lombok.Getter;
import nl.ordina.kijkdoos.dagger.AndroidApplicationModule;
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
                .androidApplicationModule(new AndroidApplicationModule(this))
                .bluetoothServiceModule(new BluetoothServiceModule())
                .build();
    }

    public static ViewBoxApplication getViewBoxApplication(Activity activity) {
        return (ViewBoxApplication) activity.getApplication();
    }
}
