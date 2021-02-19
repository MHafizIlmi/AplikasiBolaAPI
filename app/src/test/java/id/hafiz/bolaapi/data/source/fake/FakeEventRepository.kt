/*
 * hafiz on 12/18/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.fake

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.FakeTheSportDb
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Error
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.source.repository.EventRepository
import id.hafiz.bolaapi.data.source.repository.EventRepository.EventType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.coroutines.coroutineContext

@VisibleForTesting(otherwise = PRIVATE)
class FakeEventRepository : EventRepository {

  private val fakeDataSource = FakeTheSportDb()
  private val favorites = mutableMapOf<Long, Event>().apply {
    fakeDataSource.favoriteEvents().forEach { this[it.id] = it }
  }

  var shouldReturnError = false
  var responseTime = 0L

  override suspend fun get(id: Long): State<Event> {
    delay(responseTime)
    val event = fakeDataSource.eventById(id)
    return if (shouldReturnError)
      Error(R.string.msg_response_error) else Success(event)
  }

  override suspend fun getAll(
    leagueId: Long,
    type: EventType,
    badge: Boolean
  ): State<List<Event>> {
    delay(responseTime)
    val events = if (type == EventType.NEXT) fakeDataSource.eventsNextLeague()
    else fakeDataSource.eventsPastLeague()
    return if (shouldReturnError) Error(R.string.msg_response_error) else Success(events)
  }

  override suspend fun search(query: String): State<List<Event>> {
    if (shouldReturnError) {
      return Error(R.string.msg_response_error)
    }
    val events = fakeDataSource.searchEvents(query)
    return Success(events)
  }

  override suspend fun getFavorite(eventId: Long): State<Event> {
    val event = favorites[eventId] ?: return State.Empty
    return Success(event)
  }

  override suspend fun getFavorites(): State<List<Event>> {
    if (shouldReturnError) {
      // error("DON'T_PANIC: FakeEventRepository#shouldReturnError=true")
      coroutineContext[Job]?.cancel()
    }
    val events = favorites.values.toList()
    return Success(events)
  }

  override suspend fun addFavorite(event: Event): State<Boolean> {
    favorites[event.id] = event
    favorites[event.id] ?: return Error(R.string.msg_failed_add_fav)
    return Success(true)
  }

  override suspend fun removeFavorite(id: Long): State<Boolean> {
    favorites.remove(id) ?: return Error(R.string.msg_failed_remove_fav)
    return Success(true)
  }
}