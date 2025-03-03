package info.hannes.logcat

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.filters.Suppress
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class LogcatTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LogcatActivity::class.java)

    @Test
    @Suppress
    fun basicTest() {
        onView(allOf<View>(withContentDescription("Logcat"),
                withParent(withId(R.id.action_bar)),
                isDisplayed()))

        val menu = onView(withId(R.id.log_recycler))
        menu.check(ViewAssertions.matches(isDisplayed()))
    }

}
