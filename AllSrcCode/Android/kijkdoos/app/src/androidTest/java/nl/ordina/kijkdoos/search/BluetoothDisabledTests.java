package nl.ordina.kijkdoos.search;

import android.app.Activity;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import nl.ordina.kijkdoos.ViewBoxApplication;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class BluetoothDisabledTests {
    @Rule
    public ActivityTestRule<SearchViewBoxActivity> activityRule = new ActivityTestRule<SearchViewBoxActivity>(SearchViewBoxActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            ViewBoxApplication context = (ViewBoxApplication) instrumentation.getTargetContext()
                    .getApplicationContext();

            MockedApplicationComponent component = (MockedApplicationComponent) context.getApplicationComponent();
            component.inject(BluetoothDisabledTests.this);

            when(bluetoothService.isBluetoothEnabled()).thenReturn(false);

        }
    };

    @Inject
    AbstractBluetoothService bluetoothService;

    @Test
    @Ignore(value = "It is difficult to test because the searching of devices is invoked in the onResume. The " +
            "UI/UX should be changed so the enabling of Bluetooth is handled after a user interaction.")
    public void whenBluetoothIsDisabledAskUserToOpenSettings() throws Throwable {
        Intents.init();
        intending(hasAction(BluetoothAdapter.ACTION_REQUEST_ENABLE)).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null));
        activityRule.launchActivity(null);

        onView(withText("This app needs bluetooth enabled to work")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
