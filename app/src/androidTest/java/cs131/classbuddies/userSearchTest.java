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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class userSearchTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class);

    @Test
    public void userSearchTest() {
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
                allOf(withId(R.id.buddies_tab),
                        childAtPosition(
                                allOf(withId(R.id.tabs),
                                        childAtPosition(
                                                withId(R.id.linearLayout),
                                                1)),
                                2),
                        isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction relativeLayout3 = onView(
                allOf(withId(R.id.classchat_tab),
                        childAtPosition(
                                allOf(withId(R.id.tabs),
                                        childAtPosition(
                                                withId(R.id.linearLayout),
                                                1)),
                                3),
                        isDisplayed()));
        relativeLayout3.check(matches(isDisplayed()));

        ViewInteraction relativeLayout4 = onView(
                allOf(withId(R.id.signout_tab),
                        childAtPosition(
                                allOf(withId(R.id.tabs),
                                        childAtPosition(
                                                withId(R.id.linearLayout),
                                                1)),
                                7),
                        isDisplayed()));
        relativeLayout4.check(matches(isDisplayed()));

        ViewInteraction relativeLayout5 = onView(
                allOf(withId(R.id.search_tab),
                        childAtPosition(
                                allOf(withId(R.id.tabs),
                                        childAtPosition(
                                                withId(R.id.linearLayout),
                                                1)),
                                5),
                        isDisplayed()));
        relativeLayout5.check(matches(isDisplayed()));

        ViewInteraction relativeLayout6 = onView(
                allOf(withId(R.id.search_tab),
                        withParent(allOf(withId(R.id.tabs),
                                withParent(withId(R.id.linearLayout)))),
                        isDisplayed()));
        relativeLayout6.perform(click());

        ViewInteraction imageView4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.top_buddy_list),
                                0),
                        1),
                        isDisplayed()));
        imageView4.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.search_filter), withText("How would you like to filter your search?"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.background),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("How would you like to filter your search?")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView4), withText("Users"),
                        childAtPosition(
                                allOf(withId(R.id.radio_buttons),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                2)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Users")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.search_specific_class), withText("Please enter a specific class name: "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.background),
                                        1),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText("Please enter a specific class name: ")));

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.search_by_users),
                        withParent(withId(R.id.radio_buttons)),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete.perform(click());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete2.perform(click());

        ViewInteraction searchAutoComplete3 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete3.perform(replaceText("nicole"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete4 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")), withText("nicole"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete4.perform(pressImeActionButton());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Nicole Barakat"),
                        childAtPosition(
                                allOf(withId(R.id.list_view),
                                        withParent(withId(R.id.background))),
                                0),
                        isDisplayed()));


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
