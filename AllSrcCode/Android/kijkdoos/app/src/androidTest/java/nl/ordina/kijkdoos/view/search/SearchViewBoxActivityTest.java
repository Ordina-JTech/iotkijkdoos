package nl.ordina.kijkdoos.view.search;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.ViewBoxApplication;
import nl.ordina.kijkdoos.bluetooth.discovery.AbstractBluetoothDiscoveryService;
import nl.ordina.kijkdoos.bluetooth.discovery.DeviceFoundListener;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;
import nl.ordina.kijkdoos.view.control.ControlViewBoxActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class SearchViewBoxActivityTest {
    @Inject
    AbstractBluetoothDiscoveryService bluetoothService;

    private ViewBoxRemoteController mockedViewBoxRemoteController;

    @Rule
    public IntentsTestRule<SearchViewBoxActivity> activityRule = new IntentsTestRule<SearchViewBoxActivity>(SearchViewBoxActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            injectMocks();

            mockedViewBoxRemoteController = mockViewBoxRemoteController();
            mockBluetoothService();
        }

        private void injectMocks() {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            ViewBoxApplication context = (ViewBoxApplication) instrumentation.getTargetContext()
                    .getApplicationContext();

            MockedApplicationComponent component = (MockedApplicationComponent) context.getApplicationComponent();
            component.inject(SearchViewBoxActivityTest.this);
        }

        private ViewBoxRemoteController mockViewBoxRemoteController() {
            final ViewBoxRemoteController mockedViewBoxRemoteController = mock(ViewBoxRemoteController.class);
            when(mockedViewBoxRemoteController.getName()).thenReturn("Mocked JTech Kijkdoos 1");
            when(mockedViewBoxRemoteController.getAddress()).thenReturn("00:11:22:33:44:55");
            when(mockedViewBoxRemoteController.isConnected()).thenReturn(true);

            return mockedViewBoxRemoteController;
        }

        private void mockBluetoothService() {
            doAnswer(invocationOnMock -> {
                invocationOnMock.getArgumentAt(0, DeviceFoundListener.class).onDeviceFound(mockedViewBoxRemoteController);
                return null;
            }).when(bluetoothService).searchDevices(any(SearchViewBoxActivity.class));

            when(bluetoothService.isBluetoothEnabled()).thenReturn(true);
        }
    };

    @After
    public void resetMocks() throws Exception {
        Mockito.reset(bluetoothService);
    }

    @Test
    public void navigateToViewBoxActivity() throws Exception {
        onView(withText("Mocked JTech Kijkdoos 1")).perform(click());
        intended(hasComponent(ControlViewBoxActivity.class.getName()));
    }

    @Test
    public void connectToViewBox() throws Exception {
        onView(withText("Mocked JTech Kijkdoos 1")).perform(click());

        verify(mockedViewBoxRemoteController).connect(any(Context.class), any());
    }

    @Test
    public void whenTheActivityIsDisplayedThenStartTheSearchForBluetoothDevices() throws Exception {
        verify(bluetoothService).searchDevices(any(DeviceFoundListener.class));
    }

    @Test
    public void testPullToRefreshStartsASearchForDevices() throws Exception {
        onView(withId(R.id.viewBoxList)).perform(swipeDown());

        verify(bluetoothService, atLeast(2)).searchDevices(any(DeviceFoundListener.class));
    }
}
