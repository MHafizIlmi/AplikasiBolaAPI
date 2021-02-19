/*
 * hafiz on 1/2/20
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.fake

import id.hafiz.bolaapi.data.FakeTheSportDb
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Success
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.source.embedded.LeagueDataSource
import id.hafiz.bolaapi.data.source.repository.LeagueRepository

class FakeLeagueRepository : LeagueRepository {

  private val leagueDataSource = LeagueDataSource()
  private val fakeDataSource = FakeTheSportDb()

  override suspend fun get(id: Long): State<League> {
    val league = fakeDataSource.lookupLeague(id)
    return Success(league)
  }

  override suspend fun getAll(): State<List<League>> {
    return Success(leagueDataSource.getLeagues())
  }

  override suspend fun getStandings(id: Long): State<List<Standing>> {
    val standingsTable = fakeDataSource.lookupStandingsTable()
    return Success(standingsTable)
  }
}