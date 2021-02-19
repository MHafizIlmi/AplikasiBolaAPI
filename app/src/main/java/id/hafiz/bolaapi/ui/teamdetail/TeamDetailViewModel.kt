/*
 * hafiz on 12/10/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.teamdetail

import androidx.lifecycle.*
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.*
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import kotlinx.coroutines.launch

class TeamDetailViewModel(private val teamRepo: TeamRepository) : ViewModel() {

  private var currentId = 0L

  private val _team = MutableLiveData<Team>()
  val team: LiveData<Team> = _team

  private val _loading = MutableLiveData<Boolean>()
  val loading: LiveData<Boolean> = _loading

  private val _error = MutableLiveData<Int>()
  val error: LiveData<Int> = _error

  private val _favoriteMessage = MutableLiveData<Int>()
  val favoriteMessage: LiveData<Int> = _favoriteMessage

  private val _favoriteTeam = MutableLiveData<Team>()
  val favoriteIcon: LiveData<Int> = Transformations.map(_favoriteTeam) {
    if (it != null) R.drawable.ic_favorite_added else R.drawable.ic_favorite_removed
  }

  fun loadTeam(teamId: Long) {
    _favoriteMessage.value = 0
    if (teamId == currentId) return

    currentId = teamId
    _team.value = null
    _favoriteTeam.value = null
    _loading.value = true

    viewModelScope.launch {

      // check favorite
      launch { checkFavorite() }

      // get from network
      launch { getFromNetwork() }

    }.invokeOnCompletion {
      if (it != null) {
        _error.postValue(R.string.msg_user_unknown_error)
        // Log.e(javaClass.name, "loadTeam: ", it)
      }
      _loading.postValue(false)
    }
  }

  fun toggleFavorite() {
    if (loading.value == true) return
    val fetchedTeam = team.value ?: return

    _loading.value = true
    viewModelScope.launch {
      when (val state = addOrRemoveFavoriteRepo(fetchedTeam)) {
        is Success -> {
          checkFavorite()
          _favoriteMessage.postValue(state.data)
        }
        is Error -> _error.postValue(state.message)
      }
      _loading.postValue(false)
    }
  }

  // -- private -- //
  private suspend fun addOrRemoveFavoriteRepo(t: Team): State<Int> {
    return if (_favoriteTeam.value != null)
      teamRepo.removeFavorite(t.id) else teamRepo.addFavorite(t)
  }

  private suspend fun checkFavorite() {
    val favorite = teamRepo.getFavorite(currentId)
    val result = if (favorite is Success) favorite.data else null
    _favoriteTeam.postValue(result)
  }

  private suspend fun getFromNetwork() {
    // if network ok then post it
    // else if network error & favorite exist then load it
    // else post error
    when (val state = teamRepo.get(currentId)) {
      is Success -> _team.postValue(state.data)
      is Empty -> _error.postValue(R.string.msg_empty_result)
      is Error -> {
        val favoriteTeam = _favoriteTeam.value
        if (favoriteTeam != null) {
          _team.postValue(favoriteTeam)
        } else {
          _error.postValue(state.message)
        }
      }
    }
  }
}