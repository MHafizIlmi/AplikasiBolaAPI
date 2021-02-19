/*
 * hafiz on 11/8/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.entity

import com.google.gson.annotations.SerializedName
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.util.PrimaryKey

data class Event(

  @PrimaryKey
  @SerializedName("idEvent")
  override val id: Long,

  @SerializedName("strSeason")
  val session: String?,

  @SerializedName("strHomeTeam")
  val homeName: String?,

  @SerializedName("strAwayTeam")
  val awayName: String?,

  @SerializedName("intHomeScore")
  val homeScore: Int?,

  @SerializedName("intRound")
  val round: Int?,

  @SerializedName("intAwayScore")
  val awayScore: Int?,

  @SerializedName("idLeague")
  val leagueId: Long,

  @SerializedName("strHomeGoalDetails")
  val homeGoalDetails: String?,

  @SerializedName("strHomeRedCards")
  val homeRedCards: String?,

  @SerializedName("strHomeYellowCards")
  val homeYellowCards: String?,

  @SerializedName("strHomeLineupGoalkeeper")
  val homeLineupGoalkeeper: String?,

  @SerializedName("strHomeLineupDefense")
  val homeLineupDefense: String?,

  @SerializedName("strHomeLineupMidfield")
  val homeLineupMidfield: String?,

  @SerializedName("strHomeLineupForward")
  val homeLineupForward: String?,

  @SerializedName("strHomeLineupSubstitutes")
  val homeLineupSubstitutes: String?,

  @SerializedName("strHomeFormation")
  val homeFormation: String?,

  @SerializedName("strAwayRedCards")
  val awayRedCards: String?,

  @SerializedName("strAwayYellowCards")
  val awayYellowCards: String?,

  @SerializedName("strAwayGoalDetails")
  val awayGoalDetails: String?,

  @SerializedName("strAwayLineupGoalkeeper")
  val awayLineupGoalkeeper: String?,

  @SerializedName("strAwayLineupDefense")
  val awayLineupDefense: String?,

  @SerializedName("strAwayLineupMidfield")
  val awayLineupMidfield: String?,

  @SerializedName("strAwayLineupForward")
  val awayLineupForward: String?,

  @SerializedName("strAwayLineupSubstitutes")
  val awayLineupSubstitutes: String?,

  @SerializedName("strAwayFormation")
  val awayFormation: String?,

  @SerializedName("dateEvent")
  val dateEvent: String?,

  @SerializedName("dateEventLocal")
  val dateEventLocal: String?,

  @SerializedName("strDate")
  val date: String?,

  @SerializedName("strTime")
  val time: String?,

  @SerializedName("strTimeLocal")
  val timeLocal: String?,

  @SerializedName("idHomeTeam")
  val homeTeamId: Long,

  @SerializedName("idAwayTeam")
  val awayTeamId: Long,

  var homeBadgePath: String?,
  var awayBadgePath: String?,

  @SerializedName("strSport")
  val sport: String?,

  var favoriteDate: Long
) : Entity