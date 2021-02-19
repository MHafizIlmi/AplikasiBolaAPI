/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.repository

import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.entity.Standing

interface LeagueRepository {

  suspend fun get(id: Long): State<League>

  suspend fun getAll(): State<List<League>>

  suspend fun getStandings(id: Long): State<List<Standing>>
}
