package nl.ordina.kijkdoos;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by coenhoutman on 13-2-2017.
 */
@RunWith(AndroidJUnit4.class)
public class ViewBoxActivityTest {

    private ViewBoxRemoteController mockedViewBoxRemoteController;

    @Rule
    public IntentsTestRule<ViewBoxActivity> activityRule = new IntentsTestRule(ViewBoxActivity.class);

    @Test
    @Ignore("Changed the UX. No EditComponentActivityAnymore")
    public void whenClickingTheTelevisionThenDisplayTheEditActivity() throws Exception {
        onView(withId(R.id.IVTelevision)).perform(click());
    }

    @Test
    @Ignore
    public void disconnectTheViewBoxInOnPause() throws Exception {
        activityRule.getActivity().finish();
    }
}