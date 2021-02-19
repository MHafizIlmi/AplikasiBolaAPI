/*
 * hafiz on 11/12/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.data.source.repository.EventRepository
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FavoriteViewModel(
  private val eventRepo: EventRepository,
  private val teamRepo: TeamRepository
) : ViewModel() {

  private val _favorites = MutableLiveData<List<Entity>>()
  val favorites: LiveData<List<Entity>> = _favorites

  private val _loading = MutableLiveData<Boolean>()
  val loading: LiveData<Boolean> = _loading

  private val _error = MutableLiveData<Int>()
  val error: LiveData<Int> = _error

  private val favoritesSet = mutableSetOf<Entity>()
  var currentType = FavoriteType.ALL

  fun loadFavorites() {
    _loading.value = true
    viewModelScope.launch {
      when (currentType) {
        FavoriteType.EVENT -> postState(eventRepo.getFavorites())
        FavoriteType.TEAM -> postState(teamRepo.getFavorites())
        FavoriteType.ALL -> {
          postState(eventRepo.getFavorites())
          postState(teamRepo.getFavorites())
        }
      }
    }.invokeOnCompletion(::onJobsComplete)
  }

  private fun onJobsComplete(thr: Throwable?) {
    if (thr != null) {
      _error.postValue(R.string.msg_user_unknown_error)
      // Log.e(javaClass.name, "onJobsComplete: ", thr)
    }
    val result = favoritesSet.toList().sortByFavoriteDate()
    _favorites.postValue(result)
    favoritesSet.clear()
    _loading.postValue(false)
  }

  private fun CoroutineScope.postState(state: State<List<Entity>>) {
    launch {
      when (state) {
        is State.Success -> favoritesSet.addAll(state.data)
        is State.Error -> _error.postValue(state.message)
      }
    }
  }

  private fun List<Entity>.sortByFavoriteDate() = sortedWith(
    compareByDescending {
      when (it) {
        is Event -> it.favoriteDate
        is Team -> it.favoriteDate
        else -> 0
      }
    }
  )
}