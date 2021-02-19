package id.hafiz.bolaapi.ui.leaguedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.MainCoroutineRule
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.fake.FakeEventRepository
import id.hafiz.bolaapi.data.source.fake.FakeLeagueRepository
import id.hafiz.bolaapi.data.source.fake.FakeTeamRepository
import id.hafiz.bolaapi.data.source.repository.EventRepository.EventType
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
class LeagueDetailViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()


  private val leagueRepo = FakeLeagueRepository()
  private val eventRepo = FakeEventRepository()
  private val teamRepo = FakeTeamRepository()
  private val model = LeagueDetailViewModel(leagueRepo, eventRepo, teamRepo)

  private val leagueId = 4328L

  @Test
  fun leagueTest() = runBlockingTest {
    val leagueStateSource = leagueRepo.get(leagueId)

    val observer: Observer<State<League>> = mock()
    model.leagueState.observeForever(observer)

    model.loadLeague(leagueId)
    val leagueState = valueOf(model.leagueState)
    verify(observer).onChanged(leagueState)

    assertTrue(leagueState is Success)
    assertEquals(leagueStateSource.succeed, leagueState.succeed)
  }

  @Test
  fun teamsTest() = runBlockingTest {
    val teamStateSource = teamRepo.getAll(leagueId)

    val observer: Observer<State<List<Team>>> = mock()
    model.teamState.observeForever(observer)

    model.loadLeague(leagueId)
    val teamState = valueOf(model.teamState)
    verify(observer).onChanged(teamState)

    assertTrue(teamState is Success)
    assertEquals(teamStateSource.succeed, teamState.succeed)
  }

  @Test
  fun eventsTest() = runBlockingTest {
    val eventPastSource = eventRepo.getAll(leagueId, EventType.PAST)
    val eventNextSource = eventRepo.getAll(leagueId, EventType.NEXT)

    val pastObserver: Observer<State<List<Event>>> = mock()
    val nextObserver: Observer<State<List<Event>>> = mock()

    model.pastEventState.observeForever(pastObserver)
    model.nextEventState.observeForever(nextObserver)
    model.loadLeague(leagueId)

    val eventPast = valueOf(model.pastEventState)
    val eventNext = valueOf(model.nextEventState)

    verify(pastObserver).onChanged(eventPast)
    verify(nextObserver).onChanged(eventNext)

    assertTrue(eventPast is Success && eventNext is Success)
    assertEquals(eventPastSource.succeed, eventPast.succeed)
    assertEquals(eventNextSource.succeed, eventNext.succeed)
  }

  @Test
  fun standingsTest() = runBlockingTest {
    val standingsSource = leagueRepo.getStandings(leagueId)

    val observer: Observer<State<List<Standing>>> = mock()
    model.standingState.observeForever(observer)
    model.loadLeague(leagueId)

    val standings = valueOf(model.standingState)
    verify(observer).onChanged(standings)

    assertTrue(standings is Success)
    assertEquals(standingsSource.succeed, standings.succeed)
  }
}