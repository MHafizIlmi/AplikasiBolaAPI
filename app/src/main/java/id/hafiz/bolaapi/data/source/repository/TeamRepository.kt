/*
 * hafiz on 12/9/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.repository

import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.Team

interface TeamRepository {

  suspend fun get(id: Long): State<Team>

  suspend fun getAll(leagueId: Long): State<List<Team>>

  suspend fun search(query: String): State<List<Team>>

  suspend fun getFavorite(teamId: Long): State<Team>

  suspend fun getFavorites(): State<List<Team>>

  suspend fun addFavorite(team: Team): State<Int>

  suspend fun removeFavorite(teamId: Long): State<Int>
}