package nl.ordina.kijkdoos.view.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.triggertrap.seekarc.SeekArc;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.MockedViewBoxRemoteController;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.view.control.speaker.ControlSpeakerFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by coenhoutman on 13-2-2017.
 */
@RunWith(AndroidJUnit4.class)
public class ControlViewBoxActivityTest {

    private ViewBoxRemoteController mockedViewBoxRemoteController;

    @Rule
    public IntentsTestRule<ControlViewBoxActivity> activityRule = new IntentsTestRule(ControlViewBoxActivity.class) {
        @Override
        protected Intent getActivityIntent() {

            final MockedViewBoxRemoteController mockedViewBoxApplication = new MockedViewBoxRemoteController();

            final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), ControlViewBoxActivity.class);
            final Bundle bundleWrap = new Bundle();
            bundleWrap.putParcelable(ControlViewBoxActivity.EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER, Parcels.wrap(mockedViewBoxApplication));
            intent.putExtra(ControlViewBoxActivity.EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER, bundleWrap);

            return intent;
        }

        @Override
        protected void afterActivityLaunched() {
            super.afterActivityLaunched();
            mockedViewBoxRemoteController = activityRule.getActivity().getViewBoxRemoteController();
        }
    };

    @Test
    public void disconnectTheViewBoxInOnPause() throws Exception {
        activityRule.getActivity().finish();
        getInstrumentation().waitForIdleSync();

        verify(mockedViewBoxRemoteController).disconnect();
    }

    @Test
    public void testTheControlDrawerForAllControls() throws Exception {
        onView(withId(R.id.ivLeftLamp)).perform(click());
        onView(withId(R.id.component_controller)).check(matches(isOpen()));
        pressBack();
        onView(withId(R.id.component_controller)).check(matches(isClosed()));

        onView(withId(R.id.ivRightLamp)).perform(click());
        onView(withId(R.id.component_controller)).check(matches(isOpen()));
        pressBack();
        onView(withId(R.id.component_controller)).check(matches(isClosed()));

        onView(withId(R.id.ivDiscoBall)).perform(click());
        onView(withId(R.id.component_controller)).check(matches(isOpen()));
        pressBack();
        onView(withId(R.id.component_controller)).check(matches(isClosed()));

        onView(withId(R.id.ivGuitar)).perform(click());
        onView(withId(R.id.component_controller)).check(matches(isOpen()));
        pressBack();
        onView(withId(R.id.component_controller)).check(matches(isClosed()));

        onView(withId(R.id.ivTelevision)).perform(click());
        onView(withId(R.id.component_controller)).check(matches(isOpen()));
        pressBack();
        onView(withId(R.id.component_controller)).check(matches(isClosed()));
    }

    @Test
    public void givenTheLeftLampIsOffWhenSwitchingTheLeftLampThenItShouldDisplayOn() throws Exception {
        onView(ViewMatchers.withId(R.id.ivLeftLamp)).perform(click());
        onView(withId(R.id.switchLight)).check(matches(isNotChecked()))
                .perform(click()).check(matches(isChecked()));

        verify(mockedViewBoxRemoteController).toggleLeftLamp();
    }

    @Test
    public void givenTheRightLampIsOffWhenSwitchingTheRightLampThenItShouldDisplayOn() throws Exception {
        onView(withId(R.id.ivRightLamp)).perform(click());
        onView(withId(R.id.switchLight)).check(matches(isNotChecked()))
                .perform(click()).check(matches(isChecked()));

        verify(mockedViewBoxRemoteController).toggleRightLamp();
    }

    @Test
    public void testChangingColorsOfTheDiscoBall() throws Exception {
        onView(withId(R.id.ivDiscoBall)).perform(click());
        onView(withId(R.id.colorSlider)).perform(swipeRight());

        verify(mockedViewBoxRemoteController, atLeastOnce()).setDiscoBallColor(any(ControlDiscoBallFragment.DiscoBallColor.class));
    }

    @Test
    public void testSwitchingOffTheDiscoBall() throws Exception {
        onView(withId(R.id.ivDiscoBall)).perform(click());
        onView(withId(R.id.discoBallSwitch)).perform(click());

        verify(mockedViewBoxRemoteController).switchOffDiscoBall();
    }

    @Test
    public void testTheSpeaker() throws Exception {
        onView(withId(R.id.ivGuitar)).perform(click());
        onData(equalTo(ControlSpeakerFragment.Song.VADER_JACOB)).perform(click());
        onData(equalTo(ControlSpeakerFragment.Song.ALARM)).perform(click());
        onData(equalTo(ControlSpeakerFragment.Song.CUSTOM)).perform(click());

        verify(mockedViewBoxRemoteController).playSong(ControlSpeakerFragment.Song.VADER_JACOB);
        verify(mockedViewBoxRemoteController).playSong(ControlSpeakerFragment.Song.ALARM);
        verify(mockedViewBoxRemoteController).playSong(ControlSpeakerFragment.Song.CUSTOM);
    }

    @Test
    public void testRotatingTheTelevision() throws Exception {
        onView(withId(R.id.ivTelevision)).perform(click());
        onView(withId(R.id.rotationSlider)).perform(setProgress(90));

        verify(mockedViewBoxRemoteController, atLeastOnce()).rotateTelevision(anyInt());
    }

    private static ViewAction setProgress(int progress) {
        return new SetProgressOnSeekBarAction(progress);
    }

    private static class SetProgressOnSeekBarAction implements ViewAction {

        private final int progress;

        public SetProgressOnSeekBarAction(int progress) {
            this.progress = progress;
        }

        @Override
        public Matcher<View> getConstraints(){
            return isAssignableFrom(SeekArc.class);
        }


        @Override
        public String getDescription(){
            return "whatever";
        }

        @Override
        public void perform(UiController uiController, View view){
            SeekArc yourCustomView = (SeekArc) view;
            yourCustomView.setProgress(progress);
        }

    }
}