package nl.ordina.kijkdoos;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by coenhoutman on 13-2-2017.
 */
@RunWith(AndroidJUnit4.class)
public class ViewBoxActivityTest {

    @Rule
    public IntentsTestRule<ViewBoxActivity> activity = new IntentsTestRule(ViewBoxActivity.class);

    @Test
    public void whenClickingTheTelevisionThenDisplayTheEditActivity() throws Exception {
        onView(withId(R.id.IVTelevision)).perform(click());
        intended(hasComponent(EditComponentActivity.class.getName()));
    }
}