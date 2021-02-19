/*
 * hafiz on 11/11/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.data.source.repository.EventRepository
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import kotlinx.coroutines.launch

class SearchViewModel(
  private val eventRepo: EventRepository,
  private val teamRepo: TeamRepository
) : ViewModel() {

  var currentQuery = ""

  private val _result = MutableLiveData<List<Entity>>()
  val result: LiveData<List<Entity>> = _result

  private val _loading = MutableLiveData<Boolean>()
  val loading: LiveData<Boolean> = _loading

  private val _error = MutableLiveData<Int>()
  val error: LiveData<Int> = _error

  fun search(query: String) {
    if (query == currentQuery) return
    currentQuery = query
    _result.value = null
    _loading.value = true

    viewModelScope.launch {
      launch {
        when (val state = eventRepo.search(query)) {
          is State.Error -> _error.postValue(state.message)
          is State.Success -> postValue(state.data)
        }
      }

      launch {
        when (val state = teamRepo.search(query)) {
          is State.Error -> _error.postValue(state.message)
          is State.Success -> postValue(state.data)
        }
      }
    }.invokeOnCompletion {
      if (it != null) {
        _error.postValue(R.string.msg_user_unknown_error)
        Log.e(javaClass.name, "search: ", it)
      }
      _loading.postValue(false)
    }
  }

  private fun postValue(list: List<Entity>) {
    val values = _result.value?.let { it + list } ?: list
    _result.postValue(values)
  }
}