package id.hafiz.bolaapi.ui.search

import android.view.KeyEvent
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import id.hafiz.bolaapi.AsyncFragmentTest
import id.hafiz.bolaapi.R
import org.junit.Test

/**
 * @author Habib hafiz
 */
class SearchFragmentTest : AsyncFragmentTest() {

  override val fragment = SearchFragment()

  @Test
  fun searchTest() {
    onView(isAssignableFrom(EditText::class.java))
      .check(matches(isDisplayed()))
      .perform(typeText("man"), pressKey(KeyEvent.KEYCODE_ENTER))

    onView(withId(R.id.searchResults))
      .check(matches(isDisplayed()))

    onView(withText("man"))
      .check(matches(isDisplayed()))
  }
}