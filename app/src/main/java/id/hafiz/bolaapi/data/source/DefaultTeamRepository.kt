/*
 * hafiz on 12/9/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source

import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Empty
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.local.TeamDataSource
import id.hafiz.bolaapi.data.source.remote.TheSportDbService
import id.hafiz.bolaapi.data.source.remote.handleResponse
import id.hafiz.bolaapi.data.source.remote.successOf
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import id.hafiz.bolaapi.util.wrapWithIdlingResource

class DefaultTeamRepository(
  private val cacheableRemoteSource: TheSportDbService,
  private val localSource: TeamDataSource
) : TeamRepository {

  override suspend fun get(id: Long): State<Team> {
    val response = handleResponse { cacheableRemoteSource.lookupTeam(id) }
    return successOf(response) {
      val data = teams
        ?.firstOrNull { it.id == id }
        ?: return Empty
      Success(data)
    }
  }

  override suspend fun getAll(leagueId: Long): State<List<Team>> {
    val response = handleResponse { cacheableRemoteSource.lookupAllTeam(leagueId) }
    return successOf(response) {
      val data = this.teams ?: return Empty
      Success(data)
    }
  }

  override suspend fun search(query: String): State<List<Team>> {
    val response = handleResponse { cacheableRemoteSource.searchTeams(query) }
    return successOf(response) {
      val data = this.teams?.filter {
        it.sport == "Soccer"
      } ?: return Empty
      Success(data)
    }
  }

  override suspend fun getFavorite(teamId: Long): State<Team> {
    return wrapWithIdlingResource {
      val favorite = localSource.getFavorite(teamId) ?: return Empty
      Success(favorite)
    }
  }

  override suspend fun getFavorites(): State<List<Team>> {
    return wrapWithIdlingResource {
      val favorites = localSource.getFavorites()
      if (favorites.isEmpty()) return Empty
      Success(favorites)
    }
  }

  override suspend fun addFavorite(team: Team): State<Int> {
    return wrapWithIdlingResource {
      val add = localSource.saveFavorite(team)
      if (add > 0) Success(R.string.msg_ok_add_fav)
      else State.Error(R.string.msg_failed_add_fav)
    }
  }

  override suspend fun removeFavorite(teamId: Long): State<Int> {
    return wrapWithIdlingResource {
      val remove = localSource.deleteFavorite(teamId)
      if (remove > 0) Success(R.string.msg_ok_remove_fav)
      else State.Error(R.string.msg_failed_remove_fav)
    }
  }
}