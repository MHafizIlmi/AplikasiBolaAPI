/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source

import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.source.embedded.LeagueDataSource
import id.hafiz.bolaapi.data.source.remote.TheSportDbService
import id.hafiz.bolaapi.data.source.remote.handleResponse
import id.hafiz.bolaapi.data.source.remote.successOf
import id.hafiz.bolaapi.data.source.repository.LeagueRepository
import id.hafiz.bolaapi.util.wrapWithIdlingResource
import kotlinx.coroutines.delay

class DefaultLeagueRepository(
  private val embeddedSource: LeagueDataSource,
  private val cacheableRemoteSource: TheSportDbService,
  private val remoteSource: TheSportDbService
) : LeagueRepository {

  override suspend fun getAll(): State<List<League>> {
    return wrapWithIdlingResource {
      delay(500) // fake network request
      Success(embeddedSource.getLeagues())
    }
  }

  override suspend fun get(id: Long): State<League> {
    val response = handleResponse { cacheableRemoteSource.lookupLeague(id) }
    return successOf(response) {
      val data = leagues?.get(0) ?: return State.Empty
      Success(data)
    }
  }

  override suspend fun getStandings(id: Long): State<List<Standing>> {
    val response = handleResponse { remoteSource.lookupStandingsTable(id) }
    return successOf(response) {
      if (table.isNullOrEmpty()) return State.Empty
      Success(table)
    }
  }
}