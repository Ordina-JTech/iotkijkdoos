package nl.ordina.kijkdoos.viewbox.bluetooth;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by coenhoutman on 9-2-2017.
 */

@RunWith(AndroidJUnit4.class)
public class SearchViewBoxActivityTest {
    @Rule
    public ActivityTestRule<SearchViewBoxActivity> mActivityRule = new ActivityTestRule(SearchViewBoxActivity.class);

    @Test
    public void showListOfFoundViewBoxes() throws Exception {
        onView(withId(nl.ordina.kijkdoos.R.id.viewBoxList));
        onData(equalTo("JTech Kijkdoos 1")).check(matches(isDisplayed()));
    }

    @Test
    public void displayWaitingForConnectionDialog() throws Exception {
        onData(equalTo("JTech kijkdoos 1")).perform(click());
        onView(withText("Verbinden met 'JTech Kijkdoos 1'")).check(matches(isDisplayed()));
    }
}
