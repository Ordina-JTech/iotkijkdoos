package nl.ordina.kijkdoos.bluetooth;

import org.parceler.Parcel;
import org.parceler.ParcelConverter;

import static org.mockito.Mockito.mock;

/**
 * Created by coenhoutman on 27/02/2017.
 */

@Parcel(analyze = MockedViewBoxRemoteController.class,
converter = MockedViewBoxRemoteController.Converter.class)
public class MockedViewBoxRemoteController extends ViewBoxRemoteController {

    static class Converter implements ParcelConverter<MockedViewBoxRemoteController> {

        @Override
        public void toParcel(MockedViewBoxRemoteController input, android.os.Parcel parcel) {

        }

        @Override
        public MockedViewBoxRemoteController fromParcel(android.os.Parcel parcel) {
            return mock(MockedViewBoxRemoteController.class);
        }
    }
}
