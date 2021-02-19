/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.leaguedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.repository.EventRepository
import id.hafiz.bolaapi.data.source.repository.EventRepository.EventType
import id.hafiz.bolaapi.data.source.repository.LeagueRepository
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LeagueDetailViewModel(
  private val leagueRepo: LeagueRepository,
  private val eventRepo: EventRepository,
  private val teamRepo: TeamRepository
) : ViewModel() {

  private var currentLeagueId = 0L

  private val _leagueState = MutableLiveData<State<League>>()
  val leagueState: LiveData<State<League>> = _leagueState

  private val _teamState = MutableLiveData<State<List<Team>>>()
  val teamState: LiveData<State<List<Team>>> = _teamState

  private val _pastEventState = MutableLiveData<State<List<Event>>>()
  val pastEventState: LiveData<State<List<Event>>> = _pastEventState

  private val _nextEventState = MutableLiveData<State<List<Event>>>()
  val nextEventState: LiveData<State<List<Event>>> = _nextEventState

  private val _standingState = MutableLiveData<State<List<Standing>>>()
  val standingState: LiveData<State<List<Standing>>> = _standingState

  fun loadLeague(leagueId: Long) {
    if (leagueId == currentLeagueId) return
    currentLeagueId = leagueId
    initLoad()

    viewModelScope.launch {
      launch {
        _leagueState.postValue(leagueRepo.get(leagueId))
      }

      loadTeams()
      loadPastEvents()
      loadNextEvents()
      loadStandings()
    }
  }

  private fun CoroutineScope.loadTeams() = launch {
    val state = teamRepo.getAll(currentLeagueId)
    _teamState.postValue(state)
  }

  private fun CoroutineScope.loadPastEvents() = launch {
    val state = eventRepo.getAll(currentLeagueId, EventType.PAST)
    _pastEventState.postValue(state)
  }

  private fun CoroutineScope.loadNextEvents() = launch {
    val state = eventRepo.getAll(currentLeagueId, EventType.NEXT)
    _nextEventState.postValue(state)
  }

  private fun CoroutineScope.loadStandings() = launch {
    val state = leagueRepo.getStandings(currentLeagueId)
    _standingState.postValue(state)
  }

  private fun initLoad() {
    listOf(
      _leagueState, _teamState, _pastEventState,
      _nextEventState, _standingState
    ).forEach { it.value = State.Loading }
  }
}