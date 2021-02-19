/*
 * hafiz on 12/9/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.local

import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.local.base.FavoriteDataSource
import id.hafiz.bolaapi.ext.currentTimeMillis
import id.hafiz.bolaapi.util.contentValueOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.db.*

class TeamDataSource(
  private val db: ManagedSQLiteOpenHelper
) : FavoriteDataSource<Team> {

  private val tableFavoriteTeam = SportDBHelper.TABLE_FAVORITE_TEAM

  override suspend fun getFavorites(): List<Team> {
    return withContext(Dispatchers.IO) {
      db.use {
        select(tableFavoriteTeam).parseList(classParser<Team>())
      }
    }
  }

  override suspend fun getFavorite(id: Long): Team? {
    return withContext(Dispatchers.IO) {
      db.use {
        select(tableFavoriteTeam)
          .whereArgs("id = {teamId}", "teamId" to id)
          .parseOpt(classParser<Team>())
      }
    }
  }

  override suspend fun saveFavorite(value: Team): Long {
    value.favoriteDate = currentTimeMillis()
    return withContext(Dispatchers.IO) {
      val values = contentValueOf(value)
      db.use { replace(tableFavoriteTeam, *values) }
    }
  }

  override suspend fun deleteFavorite(id: Long): Int {
    return withContext(Dispatchers.IO) {
      db.use {
        delete(tableFavoriteTeam, "id = {teamId}", "teamId" to id)
      }
    }
  }
}