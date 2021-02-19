package id.hafiz.bolaapi.ui.eventdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.*
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.source.fake.FakeEventRepository
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
class EventDetailViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()

  private val eventRepo = FakeEventRepository()
  private val model = EventDetailViewModel(eventRepo)
  private val eventId = 602247L

  @Test
  fun eventStateTest() = runBlockingTest {
    val sourceState = eventRepo.get(eventId)

    val observer: Observer<State<Event>> = mock()
    model.eventState.observeForever(observer)

    model.loadEvent(eventId)
    val state = valueOf(model.eventState)

    verify(observer).onChanged(state)

    assertTrue(state is Success)
    assertEquals(sourceState.succeed, state.succeed)
  }

  @Test
  fun favoriteIconTest() {
    val observer: Observer<Int> = mock()
    model.favoriteIcon.observeForever(observer)

    model.loadEvent(eventId)
    val icon = valueOf(model.favoriteIcon)

    verify(observer).onChanged(icon)

    assertEquals(R.drawable.ic_favorite_added, icon)
  }

  @Test
  fun favoriteMessageTest() {
    val observer: Observer<Int> = mock()
    model.favoriteMessage.observeForever(observer)

    model.loadEvent(eventId)
    model.toggleFavorite()

    var message = valueOf(model.favoriteMessage)
    assertEquals(R.string.msg_ok_remove_fav, message)

    model.toggleFavorite()
    message = valueOf(model.favoriteMessage)
    assertEquals(R.string.msg_ok_add_fav, message)

    verify(observer).onChanged(message)
  }
}