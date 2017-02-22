package nl.ordina.kijkdoos.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.ordina.kijkdoos.threading.BackgroundService;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Module
public class BackgroundServiceModule {

    @Singleton
    @Provides
    public BackgroundService provideBackgroundService() {
        return new BackgroundService();
    }
}
