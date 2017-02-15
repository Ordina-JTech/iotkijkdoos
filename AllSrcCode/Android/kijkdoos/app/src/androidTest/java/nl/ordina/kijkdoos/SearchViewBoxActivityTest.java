package nl.ordina.kijkdoos;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import nl.ordina.kijkdoos.bluetooth.BluetoothService;
import nl.ordina.kijkdoos.dagger.ApplicationComponent;
import nl.ordina.kijkdoos.dagger.MockedApplicationComponent;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class SearchViewBoxActivityTest {
    @Rule
    public IntentsTestRule<SearchViewBoxActivity> mActivityRule = new IntentsTestRule(SearchViewBoxActivity.class);

    @Inject
    BluetoothService bluetoothService;

    @Before
    public void inject() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        ViewBoxApplication context = (ViewBoxApplication) instrumentation.getTargetContext()
                .getApplicationContext();

        MockedApplicationComponent component = (MockedApplicationComponent) context.getApplicationComponent();
        component.inject(this);
    }

    @Test
    public void whenTheActivityIsDisplayedThenStartTheSearchForBluetoothDevices() throws Exception {
        SearchViewBoxActivity searchViewBoxActivity = mActivityRule.getActivity();
        verify(bluetoothService).search();

    }

    @Test
    public void showListOfFoundViewBoxes() throws Exception {
        onData(equalTo("JTech Kijkdoos 1")).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToViewBoxActivity() throws Exception {
        onData(equalTo("JTech Kijkdoos 1")).perform(click());
        intended(hasComponent(ViewBoxActivity.class.getName()));
    }
}
