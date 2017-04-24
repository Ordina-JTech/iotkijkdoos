package nl.ordina.kijkdoos.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.ViewBoxApplication;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;
import nl.ordina.kijkdoos.view.search.SearchViewBoxActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.VerificationModes.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class BluetoothDisabledTests {
    @Rule
    public BluetoothDisabledActivityTestRule<SearchViewBoxActivity> searchViewBoxActivityRule =
            new BluetoothDisabledActivityTestRule<SearchViewBoxActivity>(SearchViewBoxActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    intending(hasAction(ACTION_REQUEST_ENABLE)).respondWith(new Instrumentation.ActivityResult(RESULT_CANCELED, null));
                }
            };

    @Rule
    public BluetoothDisabledActivityTestRule<BluetoothDisabledActivity> bluetoothDisabledActivityRule =
            new BluetoothDisabledActivityTestRule<>(BluetoothDisabledActivity.class);

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
        intended(hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(), SearchViewBoxActivity.class)));
    }

    private class BluetoothDisabledActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

        BluetoothDisabledActivityTestRule(Class<T> activityClass) {
            super(activityClass, true, false);
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
        }

        @Override
        protected void afterActivityFinished() {
            Intents.release();
        }
    }
}
