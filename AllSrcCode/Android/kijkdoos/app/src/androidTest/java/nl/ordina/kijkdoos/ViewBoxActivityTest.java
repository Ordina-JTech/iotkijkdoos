package nl.ordina.kijkdoos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController$$Parcelable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by coenhoutman on 13-2-2017.
 */
@RunWith(AndroidJUnit4.class)
public class ViewBoxActivityTest {

    private ViewBoxRemoteController mockedViewBoxRemoteController;

    @Rule
    public IntentsTestRule<ViewBoxActivity> activityRule = new IntentsTestRule(ViewBoxActivity.class);

    @Test
    public void whenClickingTheTelevisionThenDisplayTheEditActivity() throws Exception {
        onView(withId(R.id.IVTelevision)).perform(click());
        intended(hasComponent(EditComponentActivity.class.getName()));
    }

    @Test
    @Ignore
    public void disconnectTheViewBoxInOnPause() throws Exception {
        activityRule.getActivity().finish();
    }
}