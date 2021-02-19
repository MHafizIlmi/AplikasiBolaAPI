/*
 * hafiz on 11/12/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.local

import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.source.local.base.FavoriteDataSource
import id.hafiz.bolaapi.ext.currentTimeMillis
import id.hafiz.bolaapi.util.contentValueOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.db.*

class EventDataSource(
  private val db: ManagedSQLiteOpenHelper
) : FavoriteDataSource<Event> {

  private val tableFavoriteEvent = SportDBHelper.TABLE_FAVORITE_EVENT

  override suspend fun getFavorites(): List<Event> {
    return withContext(Dispatchers.IO) {
      db.use {
        select(tableFavoriteEvent).parseList(classParser<Event>())
      }
    }
  }

  override suspend fun getFavorite(id: Long): Event? {
    return withContext(Dispatchers.IO) {
      db.use {
        select(tableFavoriteEvent)
          .whereArgs("id = {eventId}", "eventId" to id)
          .parseOpt(classParser<Event>())
      }
    }
  }

  override suspend fun saveFavorite(value: Event): Long {
    value.favoriteDate = currentTimeMillis()
    return withContext(Dispatchers.IO) {
      val values = contentValueOf(value)
      db.use { replace(tableFavoriteEvent, *values) }
    }
  }

  override suspend fun deleteFavorite(id: Long): Int {
    return withContext(Dispatchers.IO) {
      db.use {
        delete(tableFavoriteEvent, "id = {eventId}", "eventId" to id)
      }
    }
  }
}