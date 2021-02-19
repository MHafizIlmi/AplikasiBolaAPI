/*
 * hafiz on 11/29/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.repository

import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.Event

interface EventRepository {

  enum class EventType(val value: String) {
    PAST("Latest event"), NEXT("Upcoming events")
  }

  suspend fun get(id: Long): State<Event>

  suspend fun getAll(
    leagueId: Long,
    type: EventType,
    badge: Boolean = false
  ): State<List<Event>>

  suspend fun search(query: String): State<List<Event>>

  suspend fun getFavorite(eventId: Long): State<Event>

  suspend fun getFavorites(): State<List<Event>>

  suspend fun addFavorite(event: Event): State<Boolean>

  suspend fun removeFavorite(id: Long): State<Boolean>
}