package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

/**
 * Created by coenhoutmanon 22/02/2017.
 */
@Singleton
@Component(modules = {BackgroundServiceModule.class})
public interface BackgroundComponent {
    void inject(ViewBoxRemoteController controller);
}
