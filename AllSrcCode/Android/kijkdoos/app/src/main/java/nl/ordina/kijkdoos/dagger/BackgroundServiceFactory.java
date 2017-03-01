package nl.ordina.kijkdoos.dagger;

import lombok.Getter;

/**
 * Created by coenhoutman on 22/02/2017.
 */

public class BackgroundServiceFactory {
    @Getter
    private static BackgroundComponent component = DaggerBackgroundComponent.builder()
            .backgroundServiceModule(new BackgroundServiceModule())
            .build();
}
