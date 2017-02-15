package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nl.ordina.kijkdoos.SearchViewBoxActivity;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Singleton
@Component(modules = {BluetoothServiceModule.class})
public interface ApplicationComponent {
    void inject(SearchViewBoxActivity activity);
}
