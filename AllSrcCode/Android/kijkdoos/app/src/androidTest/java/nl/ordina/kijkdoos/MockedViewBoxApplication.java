package nl.ordina.kijkdoos;

import nl.ordina.kijkdoos.dagger.ApplicationComponent;
import nl.ordina.kijkdoos.dagger.DaggerMockedApplicationComponent;
import nl.ordina.kijkdoos.dagger.MockBluetoothServiceModule;

/**
 * Created by coenhoutman on 15-2-2017.
 */

public class MockedViewBoxApplication extends ViewBoxApplication {
    @Override
    protected ApplicationComponent createApplicationComponent() {
        return DaggerMockedApplicationComponent
                .builder()
                .mockBluetoothServiceModule(new MockBluetoothServiceModule())
                .build();
    }
}
