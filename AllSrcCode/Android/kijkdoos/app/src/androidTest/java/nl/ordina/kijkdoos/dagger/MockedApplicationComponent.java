package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nl.ordina.kijkdoos.SearchViewBoxActivityTest;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Singleton
@Component(modules = {MockBluetoothServiceModule.class})
public interface MockedApplicationComponent extends ApplicationComponent {
    void inject(SearchViewBoxActivityTest activity);
}
