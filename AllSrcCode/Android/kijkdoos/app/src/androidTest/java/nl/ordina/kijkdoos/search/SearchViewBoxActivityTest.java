package nl.ordina.kijkdoos.search;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import nl.ordina.kijkdoos.ViewBoxActivity;
import nl.ordina.kijkdoos.ViewBoxApplication;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.ViewBox;
import nl.ordina.kijkdoos.bluetooth.DeviceFoundListener;
import nl.ordina.kijkdoos.bluetooth.ViewBox.OnConnectedListener;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class SearchViewBoxActivityTest {
    @Rule
    public IntentsTestRule<SearchViewBoxActivity> activityRule = new IntentsTestRule<SearchViewBoxActivity>(SearchViewBoxActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            ViewBoxApplication context = (ViewBoxApplication) instrumentation.getTargetContext()
                    .getApplicationContext();

            MockedApplicationComponent component = (MockedApplicationComponent) context.getApplicationComponent();
            component.inject(SearchViewBoxActivityTest.this);

            mockedViewBox = mock(ViewBox.class);
            when(mockedViewBox.getName()).thenReturn("Mocked JTech Kijkdoos 1");
            when(mockedViewBox.getAddress()).thenReturn("00:11:22:33:44:55");

            doAnswer(invocationOnMock -> {
                invocationOnMock.getArgumentAt(1, OnConnectedListener.class).onConnected(mockedViewBox);
                return null;
            }).when(mockedViewBox).connect(any(Context.class), any(OnConnectedListener.class));

            doAnswer(invocationOnMock -> {
                invocationOnMock.getArgumentAt(0, DeviceFoundListener.class).onDeviceFound(mockedViewBox);
                return null;
            }).when(bluetoothService).searchDevices(any(DeviceFoundListener.class));

            when(bluetoothService.isBluetoothEnabled()).thenReturn(true);
        }
    };

    @Inject
    AbstractBluetoothService bluetoothService;
    private ViewBox mockedViewBox;

    @Test
    public void navigateToViewBoxActivity() throws Exception {
        onView(withText("Mocked JTech Kijkdoos 1")).perform(click());
        intended(allOf(hasComponent(ViewBoxActivity.class.getName())));
    }

    @Test
    public void connectToViewBox() throws Exception {
        onView(withText("Mocked JTech Kijkdoos 1")).perform(click());
        verify(mockedViewBox).connect(eq(activityRule.getActivity()), any(OnConnectedListener.class));
    }

    @Test
    public void whenTheActivityIsDisplayedThenStartTheSearchForBluetoothDevices() throws Exception {
        verify(bluetoothService).searchDevices(any(DeviceFoundListener.class));
    }

    @Test
    public void whenADeviceWasAlreadyFoundIgnoreTheNewDevice() throws Exception {
        triggerAnotherSearch();

        onView(withText("Mocked JTech Kijkdoos 1")).check(matches(isDisplayed()));
    }

    private void triggerAnotherSearch() {
        activityRule.getActivity().searchForViewBoxes();
    }
}
