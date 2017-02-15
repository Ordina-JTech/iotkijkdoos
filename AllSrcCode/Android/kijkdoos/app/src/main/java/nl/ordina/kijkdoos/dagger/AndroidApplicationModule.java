package nl.ordina.kijkdoos.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by coenhoutman on 15-2-2017.
 */

@Module
public class AndroidApplicationModule {

    private final Application application;

    public AndroidApplicationModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return application;
    }
}

