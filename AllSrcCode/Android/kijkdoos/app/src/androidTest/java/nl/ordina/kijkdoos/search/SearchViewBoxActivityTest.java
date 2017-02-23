package nl.ordina.kijkdoos.search;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Future;

import javax.inject.Inject;

import nl.ordina.kijkdoos.ViewBoxActivity;
import nl.ordina.kijkdoos.ViewBoxApplication;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.DeviceFoundListener;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static nl.ordina.kijkdoos.ViewBoxActivity.EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER;
import static nl.ordina.kijkdoos.ViewBoxActivity.EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER;
import static org.hamcrest.Matchers.allOf;
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
    @Inject
    AbstractBluetoothService bluetoothService;

    private ViewBoxRemoteController mockedViewBoxRemoteController;
    private Bundle bundleAsMockForParcelableViewBoxRemoteController = new Bundle();

    @Rule
    public IntentsTestRule<SearchViewBoxActivity> activityRule = new IntentsTestRule<SearchViewBoxActivity>(SearchViewBoxActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            injectMocks();

            mockedViewBoxRemoteController = mockViewBoxRemoteController();
            mockBluetoothService();
        }

        @Override
        protected void afterActivityLaunched() {
            super.afterActivityLaunched();
            mockViewBoxActivityNavigation();
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
            when(mockedViewBoxRemoteController.wrapInParcelable()).thenReturn(bundleAsMockForParcelableViewBoxRemoteController);
            when(mockedViewBoxRemoteController.connect(any())).thenReturn(mock(Future.class));

            return mockedViewBoxRemoteController;
        }

        private void mockBluetoothService() {
            doAnswer(invocationOnMock -> {
                invocationOnMock.getArgumentAt(0, DeviceFoundListener.class).onDeviceFound(mockedViewBoxRemoteController);
                return null;
            }).when(bluetoothService).searchDevices(any(SearchViewBoxActivity.class));

            when(bluetoothService.isBluetoothEnabled()).thenReturn(true);
        }

        private void mockViewBoxActivityNavigation() {
            final ActivityResult mockViewBoxActivityInvocation = new ActivityResult(-1, null);
            intending(hasComponent(ViewBoxActivity.class.getName())).respondWith(mockViewBoxActivityInvocation);
        }
    };

    @Test
    public void navigateToViewBoxActivity() throws Exception {
        Bundle expectedIntentBundle = new Bundle();
        expectedIntentBundle.putParcelable(EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER, new Bundle());

        onView(withText("Mocked JTech Kijkdoos 1")).perform(click());
        intended(allOf(hasComponent(ViewBoxActivity.class.getName()),
                hasExtra(org.hamcrest.Matchers.equalTo(EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER), org.hamcrest.Matchers.any(Bundle.class))));
    }

    @Test
    public void connectToViewBox() throws Exception {
        onView(withText("Mocked JTech Kijkdoos 1")).perform(click());

        verify(mockedViewBoxRemoteController).connect(eq(activityRule.getActivity()));
    }

    @Test
    public void whenTheActivityIsDisplayedThenStartTheSearchForBluetoothDevices() throws Exception {
        verify(bluetoothService).searchDevices(any(DeviceFoundListener.class));
    }
}
