package nl.ordina.kijkdoos;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.ordina.kijkdoos.SearchViewBoxActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class SearchViewBoxActivityTest {
    @Rule
    public IntentsTestRule<SearchViewBoxActivity> mActivityRule = new IntentsTestRule(SearchViewBoxActivity.class);

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
