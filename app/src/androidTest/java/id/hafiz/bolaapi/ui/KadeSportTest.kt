/*
 * hafiz on 1/5/20
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui

import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import id.hafiz.bolaapi.AsyncUIOperation
import id.hafiz.bolaapi.CustomMatchers.tabTitle
import id.hafiz.bolaapi.CustomMatchers.tabsCount
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.ui.common.EntityListAdapter.EntityViewHolder
import id.hafiz.bolaapi.ui.team.TeamView
import id.hafiz.bolaapi.view.EventView
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

class bolaapiTest : AsyncUIOperation() {

  @get:Rule
  val activityRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun app() {
    // tabs
    onView(withId(R.id.homeTabLayout))
      .check(
        matches(
          allOf(
            tabsCount(2),
            tabTitle(0, R.string.title_league),
            tabTitle(1, R.string.title_favorite)
          )
        )
      )

    // league
    onView(allOf(withId(R.id.leagueGrid), isDisplayed()))
      .perform(actionOnItemAtPosition<EntityViewHolder<*>>(0, click()))

    // league detail
    onView(withId(R.id.itemTitle))
      .check(matches(isDisplayed()))

    onView(withText("About this league"))
      .check(matches(isDisplayed()))
      .perform(click())

    onView(allOf(isAssignableFrom(Button::class.java), withText("OK")))
      .check(matches(isDisplayed()))
      .perform(click())

    // teams
    onView(withText("All teams"))
      .check(matches(isDisplayed()))
      .perform(click())

    onView(withId(R.id.teamList))
      .check(matches(isDisplayed()))
      .perform(actionOnItemAtPosition<EntityViewHolder<TeamView>>(0, click()))

    // team detail & favorite toggling
    onView(withId(R.id.itemBadge))
      .check(matches(isDisplayed()))

    onView(withId(R.id.itemTitle))
      .check(matches(isDisplayed()))

    onView(withText("About this team"))
      .check(matches(isDisplayed()))

    toggleFavorite()

    // navigate to league detail
    pressBack()
    pressBack()

    onView(withId(R.id.leagueDetailFragment))
      .perform(swipeUp())

    // classement table
    onView(withText("Classement table"))
      .check(matches(isDisplayed()))
      .perform(click())

    // events
    onView(withText("Upcoming events"))
      .check(matches(isDisplayed()))
      .perform(click())

    onView(withId(R.id.eventList))
      .check(matches(isDisplayed()))
      .perform(actionOnItemAtPosition<EntityViewHolder<EventView>>(0, click()))

    // event detail & favorite toggling
    onView(withId(R.id.badgeHome))
      .check(matches(isDisplayed()))

    onView(withId(R.id.titleHome))
      .check(matches(isDisplayed()))

    onView(withId(R.id.badgeAway))
      .check(matches(isDisplayed()))

    onView(withId(R.id.titleAway))
      .check(matches(isDisplayed()))

    toggleFavorite()

    // navigate to favorite tab
    pressBack()
    pressBack()
    pressBack()

    onView(withText(R.string.title_favorite))
      .check(matches(isDisplayed()))
      .perform(click())

    // favorite event or team
    try {
      onView(allOf(withId(R.id.favoriteList), isDisplayed()))
        .perform(actionOnItemAtPosition<EntityViewHolder<TeamView>>(0, click()))
    } catch (e: PerformException) {
      // in case favorites is empty
      onView(withText(R.string.msg_empty_result))
        .check(matches(isDisplayed()))
    }

    // navigate to search
    onView(withId(R.id.menuSearch))
      .check(matches(isDisplayed()))
      .perform(click())

    // search
    onView(isAssignableFrom(EditText::class.java))
      .check(matches(isDisplayed()))
      .perform(typeText("man "), pressKey(KeyEvent.KEYCODE_ENTER))

    onView(withId(R.id.searchResults))
      .check(matches(isDisplayed()))
      .perform(swipeUp())
  }

  private fun toggleFavorite() {
    onView(withId(R.id.menuFavorite))
      .check(matches(isDisplayed()))
      .perform(click())

    try {
      onView(withText(R.string.msg_ok_add_fav))
        .check(matches(isDisplayed()))
    } catch (e: NoMatchingViewException) {
      onView(withText(R.string.msg_ok_remove_fav))
        .check(matches(isDisplayed()))
    }
  }
}