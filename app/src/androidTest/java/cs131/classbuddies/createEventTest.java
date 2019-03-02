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
public class createEventTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class);

    @Test
    public void createEventTest() {
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

        ViewInteraction imageView3 = onView(
                allOf(withId(R.id.img_create_account),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        4),
                                0),
                        isDisplayed()));
        imageView3.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name_field), isDisplayed()));
        appCompatEditText.perform(replaceText("classbuddies"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.pass_field), isDisplayed()));
        appCompatEditText2.perform(replaceText("classbuds"), closeSoftKeyboard());

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
                allOf(withId(R.id.home_tab),
                        childAtPosition(
                                allOf(withId(R.id.tabs),
                                        childAtPosition(
                                                withId(R.id.linearLayout),
                                                1)),
                                0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction relativeLayout2 = onView(
                allOf(withId(R.id.meetup_tab),
                        childAtPosition(
                                allOf(withId(R.id.tabs),
                                        childAtPosition(
                                                withId(R.id.linearLayout),
                                                1)),
                                4),
                        isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction relativeLayout3 = onView(
                allOf(withId(R.id.meetup_tab),
                        withParent(allOf(withId(R.id.tabs),
                                withParent(withId(R.id.linearLayout)))),
                        isDisplayed()));
        relativeLayout3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextName), isDisplayed()));
        appCompatEditText4.perform(replaceText("my study group"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextClass), isDisplayed()));
        appCompatEditText5.perform(replaceText("CSC131"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextClass), withText("CSC131"), isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinMonth), isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Dec"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.spinDay), isDisplayed()));
        appCompatSpinner2.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("11"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.spinYear), isDisplayed()));
        appCompatSpinner3.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("2017"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.editTextLocationDesc), isDisplayed()));
        appCompatEditText11.perform(replaceText("Riverside Hall Lobbby"), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.editTextDescription), isDisplayed()));
        appCompatEditText12.perform(replaceText("Study for finals"), closeSoftKeyboard());

        ViewInteraction textView = onView(
                allOf(withId(R.id.txtDescLocation), withText("Describe your location:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.page1),
                                        1),
                                19),
                        isDisplayed()));
        textView.check(matches(withText("Describe your location:")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.txtMeetUpDesc), withText("Describe Your Meet Up:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.page1),
                                        1),
                                21),
                        isDisplayed()));
        textView2.check(matches(withText("Describe Your Meet Up:")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.txtClass), withText("Class:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.page1),
                                        1),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Class:")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.txtClass), withText("Class:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.page1),
                                        1),
                                2),
                        isDisplayed()));
        textView4.check(matches(withText("Class:")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.txtEvent), withText("Event:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.page1),
                                        1),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Event:")));

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
