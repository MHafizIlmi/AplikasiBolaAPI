package id.hafiz.bolaapi.ui.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.*
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Error
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.source.fake.FakeEventRepository
import id.hafiz.bolaapi.data.source.repository.EventRepository.EventType
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
class EventViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()

  private val eventRepo = FakeEventRepository()
  private val model = EventViewModel(eventRepo)
  private val leagueId = 4328L

  @Test
  fun `eventStateTest Success`() = runBlockingTest {
    val sourceState = eventRepo.getAll(leagueId, EventType.NEXT)

    val observer: Observer<State<List<Event>>> = mock()
    model.eventsState.observeForever(observer)

    model.loadEvents(leagueId, EventType.NEXT)
    val state = valueOf(model.eventsState)

    verify(observer).onChanged(state)

    assertTrue(state is Success)
    assertEquals(sourceState.succeed, state.succeed)
  }

  @Test
  fun `eventStateTest Error`() = runBlockingTest {
    eventRepo.shouldReturnError = true
    val sourceState = eventRepo.getAll(leagueId, EventType.PAST)

    val observer: Observer<State<List<Event>>> = mock()
    model.eventsState.observeForever(observer)

    model.loadEvents(leagueId, EventType.PAST)
    val state = valueOf(model.eventsState)

    verify(observer).onChanged(state)

    assertTrue(state is Error)
    assertEquals(sourceState.error, state.error)
  }

  @Test
  fun notifierTest() {
    eventRepo.responseTime = 7_000 + 1

    val observer: Observer<Boolean> = mock()
    model.notifier.observeForever(observer)

    coroutineRule.pauseDispatcher()

    model.loadEvents(leagueId, EventType.PAST)
    coroutineRule.resumeDispatcher()

    val notifier = valueOf(model.notifier)
    verify(observer).onChanged(notifier)
  }
}