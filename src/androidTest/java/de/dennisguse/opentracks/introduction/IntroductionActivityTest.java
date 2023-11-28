
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import de.dennisguse.opentracks.debug.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntroductionActivityTest {

    @Rule
    public ActivityScenarioRule<IntroductionActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(IntroductionActivity.class);

    @Test
    public void introductionActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.track_list_fab_action), withContentDescription("Record"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("Speed"),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText("Speed")));

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("Heart Rate"),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView2.check(matches(withText("Heart Rate")));

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.text1), withText("Distance"),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView3.check(matches(withText("Distance")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
