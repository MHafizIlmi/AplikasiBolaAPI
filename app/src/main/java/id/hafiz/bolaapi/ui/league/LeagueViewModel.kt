/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.league

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.Loading
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.source.repository.LeagueRepository
import kotlinx.coroutines.launch

class LeagueViewModel(private val leagueRepo: LeagueRepository) : ViewModel() {

  private val _leagues = MutableLiveData<State<List<League>>>()
  val leagues: LiveData<State<List<League>>> = _leagues

  init {
    loadLeagues()
  }

  private fun loadLeagues() {
    _leagues.value = Loading
    viewModelScope.launch {
      val state = leagueRepo.getAll()
      _leagues.postValue(state)
    }
  }
}