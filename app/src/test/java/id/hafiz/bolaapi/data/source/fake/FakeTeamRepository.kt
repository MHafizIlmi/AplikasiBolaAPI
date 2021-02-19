/*
 * hafiz on 1/1/20
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.fake

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.FakeTheSportDb
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.repository.TeamRepository

@VisibleForTesting(otherwise = PRIVATE)
class FakeTeamRepository : TeamRepository {

  private val fakeDataSource = FakeTheSportDb()
  private val favorites = mutableMapOf<Long, Team>().apply {
    fakeDataSource.favoriteTeams().forEach { this[it.id] = it }
  }

  var shouldReturnError = false

  override suspend fun get(id: Long): State<Team> {
    if (shouldReturnError) {
      return State.Error(R.string.msg_response_error)
    }
    val team = fakeDataSource.lookupTeam(id)
    return Success(team)
  }

  override suspend fun getAll(leagueId: Long): State<List<Team>> {
    val teams = fakeDataSource.lookupAllTeams()
    return Success(teams)
  }

  override suspend fun search(query: String): State<List<Team>> {
    val teams = fakeDataSource.searchTeam(query)
    return Success(teams)
  }

  override suspend fun getFavorite(teamId: Long): State<Team> {
    val team = favorites[teamId] ?: return State.Empty
    return Success(team)
  }

  override suspend fun getFavorites(): State<List<Team>> {
    val teams = favorites.values.toList()
    return Success(teams)
  }

  override suspend fun addFavorite(team: Team): State<Int> {
    favorites[team.id] = team
    favorites[team.id] ?: return State.Error(R.string.msg_failed_add_fav)
    return Success(R.string.msg_ok_add_fav)
  }

  override suspend fun removeFavorite(teamId: Long): State<Int> {
    favorites.remove(teamId) ?: return State.Error(R.string.msg_failed_remove_fav)
    return Success(R.string.msg_ok_remove_fav)
  }
}