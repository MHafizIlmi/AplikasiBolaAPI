/*
 * hafiz on 11/23/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.tabs.TabLayout
import id.hafiz.bolaapi.util.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule

object CustomAssertions {

  fun recyclerViewItemCount(expected: Int) = ViewAssertion { view, ex ->
    if (ex != null) throw ex // no view found
    if (view !is RecyclerView) throw IllegalArgumentException("Not RecyclerView")
    val adapter = view.adapter
    assertNotNull(adapter)
    assertThat(adapter?.itemCount, `is`(expected))
  }
}

object CustomMatchers {

  fun tabsCount(expectedCount: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description?) {
        description?.appendText("TabLayout with tabs count $expectedCount")
      }

      override fun matchesSafely(item: View?) =
        item is TabLayout && `is`(expectedCount).matches(item.tabCount)
    }
  }

  fun tabTitle(position: Int, @StringRes expectedTitle: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description?) {
        description?.appendText("Tab with title res $expectedTitle")
      }

      override fun matchesSafely(item: View?): Boolean {
        return item is TabLayout && `is`(item.context.getString(expectedTitle))
          .matches(item.getTabAt(position)?.text)
      }
    }
  }

  fun atIndex(matcher: Matcher<View>, index: Int): TypeSafeMatcher<View> {
    return object : TypeSafeMatcher<View>() {

      private var current = 0

      override fun describeTo(description: Description?) {
        description?.appendText("at index: $index ")
        matcher.describeTo(description)
      }

      override fun matchesSafely(item: View?): Boolean {
        return matcher.matches(item) && current++ == index
      }
    }
  }
}

abstract class AsyncUIOperation {

  @Before
  fun register() {
    IdlingRegistry.getInstance()
      .register(EspressoIdlingResource.idlingResource)
  }

  @After
  fun unregister() {
    IdlingRegistry.getInstance()
      .unregister(EspressoIdlingResource.idlingResource)
  }
}

abstract class AsyncFragmentTest : AsyncUIOperation() {

  @get:Rule
  val activityRule = ActivityTestRule(SingleFragmentActivity::class.java)

  abstract val fragment: Fragment

  @Before
  fun setup() {
    activityRule.activity.bind(fragment)
  }
}

inline fun <reified T : Activity> activityTestRuleIntentOf(
  noinline block: Intent.() -> Unit
) = object : ActivityTestRule<T>(T::class.java) {
  override fun getActivityIntent(): Intent? {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val intent = Intent(context, T::class.java)
    block(intent)
    return intent
  }
}