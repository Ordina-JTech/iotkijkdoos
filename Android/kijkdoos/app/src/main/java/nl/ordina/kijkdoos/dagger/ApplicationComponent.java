package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nl.ordina.kijkdoos.bluetooth.BluetoothConnectionFragment;
import nl.ordina.kijkdoos.view.control.ControlViewBoxActivity;
import nl.ordina.kijkdoos.view.search.SearchViewBoxActivity;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Singleton
@Component(modules = {AndroidApplicationModule.class,
        BluetoothServiceModule.class})
public interface ApplicationComponent {
    void inject(SearchViewBoxActivity activity);
    void inject(ControlViewBoxActivity activity);

    void inject(BluetoothConnectionFragment fragment);
}
