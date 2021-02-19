package id.hafiz.bolaapi.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.ext.currentTimeMillis
import id.hafiz.bolaapi.util.createColumns
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.createTable

class SportDBHelper(context: Context, dbName: String) :
  ManagedSQLiteOpenHelper(context, dbName, version = 2) {

  override fun onCreate(db: SQLiteDatabase?) {
    createTableFavoriteEvent(db)
    createTableFavoriteTeam(db)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldV: Int, newV: Int) {
    if (newV > oldV && newV == 2) {
      createTableFavoriteTeam(db)

      val time = currentTimeMillis()
      db?.execSQL("ALTER TABLE $TABLE_FAVORITE_EVENT ADD COLUMN favoriteDate INTEGER DEFAULT $time")
    }
  }

  private fun createTableFavoriteEvent(db: SQLiteDatabase?) {
    val eventColumns = createColumns<Event>()
    db?.createTable(TABLE_FAVORITE_EVENT, true, *eventColumns)
  }

  private fun createTableFavoriteTeam(db: SQLiteDatabase?) {
    val teamColumns = createColumns<Team>()
    db?.createTable(TABLE_FAVORITE_TEAM, true, *teamColumns)
  }

  companion object {
    const val TABLE_FAVORITE_EVENT = "favorite_events"
    const val TABLE_FAVORITE_TEAM = "favorite_team"
  }
}
