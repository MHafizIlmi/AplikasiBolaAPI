/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi

import android.app.Application

class KadeApplication : Application() {

  val repositories: Map<String, Any>
    get() = mapOf(
      "leagueRepository" to ServiceLocator.provideLeagueRepository(this),
      "eventRepository" to ServiceLocator.provideEventRepository(this),
      "teamRepository" to ServiceLocator.provideTeamRepository(this)
    )
}