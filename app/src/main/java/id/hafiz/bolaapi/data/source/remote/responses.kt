/*
 * hafiz on 11/2/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.remote

import android.util.Log
import com.google.gson.annotations.SerializedName
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.remote.ResponseResult.Failure
import id.hafiz.bolaapi.data.source.remote.ResponseResult.Success
import id.hafiz.bolaapi.util.EspressoIdlingResource
import retrofit2.Response

data class LeagueResponse(
  val leagues: List<League>?
)

data class LeagueEventResponse(
  // Search will using alternate value although the result is json array.
  @SerializedName(value = "events", alternate = ["event"])
  val events: List<Event>?
)

data class TeamResponse(
  val teams: List<Team>?
)

data class StandingsTableResponse(
  val table: List<Standing>?
)

sealed class ResponseResult<out R> {
  class Success<out T>(val data: T) : ResponseResult<T>()
  class Failure(val message: Int) : ResponseResult<Nothing>()
}

inline fun <reified T> handleResponse(response: () -> Response<T>): ResponseResult<T> {
  EspressoIdlingResource.increment()
  return try {
    val res = response()
    if (res.isSuccessful) {
      val result = res.body() ?: return Failure(R.string.msg_response_empty)
      Success(result)
    } else {
      Failure(R.string.msg_response_failure)
    }
  } catch (e: Exception) {
    Log.e(T::class.qualifiedName, "handleResponse: ", e)
    Failure(R.string.msg_response_error)
  } finally {
    EspressoIdlingResource.decrement()
  }
}

inline fun <T, R> successOf(
  response: ResponseResult<T>,
  success: T.() -> State.Success<R>
) = when (response) {
  is Success -> success(response.data)
  is Failure -> State.Error(response.message)
}