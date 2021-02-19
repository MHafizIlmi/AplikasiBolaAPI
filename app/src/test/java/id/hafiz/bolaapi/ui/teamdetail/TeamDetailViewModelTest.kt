package id.hafiz.bolaapi.ui.teamdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.*
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.fake.FakeTeamRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

/**
 * @author Habib hafiz
 */
@ExperimentalCoroutinesApi
class TeamDetailViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()

  private val teamRepo = FakeTeamRepository()
  private val model = TeamDetailViewModel(teamRepo)
  private val teamId = 133612L

  @Test
  fun loadTeamTest() = runBlockingTest {
    val teamSource = teamRepo.get(teamId).succeed

    val observer: Observer<Team> = mock()
    model.team.observeForever(observer)
    model.loadTeam(teamId)
    val team = valueOf(model.team)
    verify(observer).onChanged(team)

    assertEquals(teamSource, team)
  }

  @Test
  fun loadingTest() {
    coroutineRule.pauseDispatcher()
    val observer: Observer<Boolean> = mock()
    model.loading.observeForever(observer)

    model.loadTeam(teamId)
    val loadingBefore = valueOf(model.loading)
    verify(observer).onChanged(loadingBefore)
    assertTrue(loadingBefore)

    coroutineRule.resumeDispatcher()
    val loadingAfter = valueOf(model.loading)
    verify(observer).onChanged(loadingAfter)
    assertFalse(loadingAfter)
  }

  @Test
  fun errorTest() {
    teamRepo.shouldReturnError = true

    val observer: Observer<Int> = mock()
    model.error.observeForever(observer)

    model.loadTeam(teamId)
    val error = valueOf(model.error)
    verify(observer).onChanged(R.string.msg_response_error)

    assertEquals(R.string.msg_response_error, error)
  }

  @Test
  fun favoriteIconTest() {
    val observer: Observer<Int> = mock()
    model.favoriteIcon.observeForever(observer)
    model.loadTeam(teamId)
    val icon = valueOf(model.favoriteIcon)
    verify(observer, times(2)).onChanged(icon)
    assertEquals(R.drawable.ic_favorite_removed, icon)
  }

  @Test
  fun favoriteMessageTest() {
    val observer: Observer<Int> = mock()
    model.favoriteMessage.observeForever(observer)

    model.loadTeam(teamId)
    model.toggleFavorite()

    var message = valueOf(model.favoriteMessage)
    assertEquals(R.string.msg_ok_add_fav, message)

    model.toggleFavorite()
    message = valueOf(model.favoriteMessage)
    assertEquals(R.string.msg_ok_remove_fav, message)

    verify(observer).onChanged(message)
  }
}