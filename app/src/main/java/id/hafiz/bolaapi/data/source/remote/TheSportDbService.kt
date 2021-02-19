/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheSportDbService {

  @GET("lookupleague.php")
  suspend fun lookupLeague(@Query("id") leagueId: Long): Response<LeagueResponse>

  @GET("eventsnextleague.php")
  suspend fun eventsNextLeague(@Query("id") leagueId: Long): Response<LeagueEventResponse>

  @GET("eventspastleague.php")
  suspend fun eventsPastLeague(@Query("id") leagueId: Long): Response<LeagueEventResponse>

  @GET("lookupevent.php")
  suspend fun lookupEvent(@Query("id") eventId: Long): Response<LeagueEventResponse>

  @GET("searchevents.php")
  suspend fun searchEvents(@Query("e") query: String): Response<LeagueEventResponse>

  @GET("lookup_all_teams.php")
  suspend fun lookupAllTeam(@Query("id") leagueId: Long): Response<TeamResponse>

  @GET("lookupteam.php")
  suspend fun lookupTeam(@Query("id") teamId: Long): Response<TeamResponse>

  @GET("searchteams.php")
  suspend fun searchTeams(@Query("t") query: String): Response<TeamResponse>

  @GET("lookuptable.php")
  suspend fun lookupStandingsTable(@Query("l") leagueId: Long): Response<StandingsTableResponse>
}