package id.hafiz.bolaapi.ui.team

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.MainCoroutineRule
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.fake.FakeTeamRepository
import id.hafiz.bolaapi.mock
import id.hafiz.bolaapi.succeed
import id.hafiz.bolaapi.valueOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify

/**
 * @author Habib hafiz
 */
@ExperimentalCoroutinesApi
class TeamViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()

  private val teamRepo = FakeTeamRepository()
  private val model = TeamViewModel(teamRepo)
  private val leagueId = 4328L

  @Test
  fun loadTeamsTest() = runBlockingTest {
    val teamsSource = teamRepo.getAll(leagueId)

    val observer: Observer<State<List<Team>>> = mock()
    model.teamState.observeForever(observer)
    model.loadTeams(leagueId)
    val teamsState = valueOf(model.teamState)
    verify(observer).onChanged(teamsState)

    assertTrue(teamsState is Success)
    assertEquals(teamsSource.succeed, teamsState.succeed)
  }
}