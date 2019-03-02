package cs131.classbuddies;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BuddiesListTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class);

    @Test
    public void buddiesListTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name_field), isDisplayed()));
        appCompatEditText.perform(replaceText("classbuddies"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.pass_field), isDisplayed()));
        appCompatEditText2.perform(replaceText("classbuds"), closeSoftKeyboard());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.logo),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.coord_layout),
                                        0),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.img_log_in),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        3),
                                0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.pass_field), withText("classbuds"), isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.img_log_in), isDisplayed()));
        appCompatImageView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button = onView(
                allOf(withId(R.id.menu_button), withText("Button"), isDisplayed()));
        button.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.buddies_tab),
                        withParent(allOf(withId(R.id.tabs),
                                withParent(withId(R.id.linearLayout)))),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("Robert Roati"),
                        childAtPosition(
                                allOf(withId(R.id.list_view),
                                        childAtPosition(
                                                withId(R.id.background),
                                                1)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Robert Roati")));

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Robert Roati"),
                        childAtPosition(
                                allOf(withId(R.id.list_view),
                                        withParent(withId(R.id.background))),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.name), withText("Robert Roati"),
                        childAtPosition(
                                allOf(withId(R.id.profile_box),
                                        childAtPosition(
                                                withId(R.id.profile),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Robert Roati")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.major), withText("Retirement Studies"),
                        childAtPosition(
                                allOf(withId(R.id.profile_box),
                                        childAtPosition(
                                                withId(R.id.profile),
                                                0)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Retirement Studies")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.alter_buddy_text), withText("Remove Buddy"),
                        childAtPosition(
                                allOf(withId(R.id.alter_buddy),
                                        childAtPosition(
                                                withId(R.id.buddy_profile),
                                                2)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Remove Buddy")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.message_text), withText("Message"),
                        childAtPosition(
                                allOf(withId(R.id.message_buddy),
                                        childAtPosition(
                                                withId(R.id.buddy_profile),
                                                3)),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("Message")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.classes_header), withText("Classes"),
                        childAtPosition(
                                allOf(withId(R.id.classes),
                                        childAtPosition(
                                                withId(R.id.buddy_profile),
                                                4)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Classes")));

        pressBack();

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
