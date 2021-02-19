package id.hafiz.bolaapi.ui.league

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.MainCoroutineRule
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.source.fake.FakeLeagueRepository
import id.hafiz.bolaapi.mock
import id.hafiz.bolaapi.succeed
import id.hafiz.bolaapi.valueOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify

/**
 * @author Habib hafiz
 */
@ExperimentalCoroutinesApi
class LeagueViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()

  private val leagueRepo = FakeLeagueRepository()

  @Test
  fun loadLeaguesTest() {
    val model = LeagueViewModel(leagueRepo)

    val observer: Observer<State<List<League>>> = mock()
    model.leagues.observeForever(observer)

    val leagues = valueOf(model.leagues)
    verify(observer).onChanged(leagues)

    assertTrue(leagues is Success)
    assertTrue(leagues.succeed.isNotEmpty())
  }
}