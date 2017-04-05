package nl.ordina.kijkdoos.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.VerificationMode;
import android.support.test.espresso.intent.VerificationModes;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import javax.inject.Inject;

import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.ViewBoxApplication;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.MockedViewBoxRemoteController;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;
import nl.ordina.kijkdoos.view.control.ControlViewBoxActivity;
import nl.ordina.kijkdoos.view.search.SearchViewBoxActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.VerificationModes.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class BluetoothDisabledTests {
    @Rule
    public BluetoothDisabledActivityTestRule<SearchViewBoxActivity> searchViewBoxActivityRule =
            new BluetoothDisabledActivityTestRule<>(SearchViewBoxActivity.class, UserResponse.DONT_ENABLE_BLUETOOTH);

    @Rule
    public BluetoothDisabledActivityTestRule<BluetoothDisabledActivity> bluetoothDisabledActivityRule =
            new BluetoothDisabledActivityTestRule<>(BluetoothDisabledActivity.class, UserResponse.ENABLE_BLUETOOTH);

    @Inject
    AbstractBluetoothService bluetoothService;

    @Test
    public void givenBluetoothIsDisabledWhenSearchingForDevicesThenDisplayActivityThatBluetoothIsDisabled() throws Throwable {
        searchViewBoxActivityRule.launchActivity(null);

        intended(hasComponent(BluetoothDisabledActivity.class.getName()), times(2));
    }

    @Test
    public void whenUserClicksOnTheButtonToEnableBluetoothThenNavigateToTheSearchActivity() throws Exception {
        bluetoothDisabledActivityRule.launchActivity(null);

        onView(withId(R.id.enableBluetoothButton)).perform(click());
        intended(hasComponent(SearchViewBoxActivity.class.getName()));
    }

    enum UserResponse {
        ENABLE_BLUETOOTH(Activity.RESULT_OK), DONT_ENABLE_BLUETOOTH(Activity.RESULT_CANCELED);

        private final int userResponse;

        UserResponse(int userResponse) {
            this.userResponse = userResponse;
        }
    }

    private class BluetoothDisabledActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

        private final UserResponse userResponse;

        BluetoothDisabledActivityTestRule(Class<T> activityClass, UserResponse userResponse) {
            super(activityClass, true, false);
            this.userResponse = userResponse;
        }

        @Override
        protected void beforeActivityLaunched() {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            ViewBoxApplication context = (ViewBoxApplication) instrumentation.getTargetContext()
                    .getApplicationContext();

            MockedApplicationComponent component = (MockedApplicationComponent) context.getApplicationComponent();
            component.inject(BluetoothDisabledTests.this);

            when(bluetoothService.isBluetoothEnabled()).thenReturn(false);

            Intents.init();
            intending(hasAction(BluetoothAdapter.ACTION_REQUEST_ENABLE)).respondWith(new Instrumentation.ActivityResult(userResponse.userResponse, null));
        }

        @Override
        protected void afterActivityFinished() {
            Intents.release();
        }
    }
}
