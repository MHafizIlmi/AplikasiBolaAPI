/*
 * hafiz on 11/8/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.eventdetail

import androidx.lifecycle.*
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.State.*
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.source.repository.EventRepository
import kotlinx.coroutines.launch

class EventDetailViewModel(
  private val eventRepo: EventRepository
) : ViewModel() {

  private var event: Event? = null
  private val isInFavorite = MutableLiveData<Boolean>()

  private val _eventState = MutableLiveData<State<Event>>()
  val eventState: LiveData<State<Event>> = _eventState

  private val _favoriteMessage = MutableLiveData<Int>()
  val favoriteMessage: LiveData<Int> = _favoriteMessage

  val favoriteIcon: LiveData<Int> = Transformations.map(isInFavorite) {
    if (it) R.drawable.ic_favorite_added else R.drawable.ic_favorite_removed
  }

  fun loadEvent(eventId: Long) {
    _favoriteMessage.value = 0
    if (_eventState.value != null && event?.id == eventId) return

    isInFavorite.value = false
    _eventState.value = Loading
    viewModelScope.launch {
      // check favorite
      val localEvent = checkFavorite(eventId)

      // fetch from network
      val network = eventRepo.get(eventId)
      _eventState.postValue(network)

      when (network) {
        // if success fill local event
        is Success -> event = network.data
        // if fail get from local database
        is Error -> {
          val result = localEvent?.let {
            event = it
            Success(it)
          } ?: Error(network.message)
          _eventState.postValue(result)
        }
      }
    }
  }

  fun toggleFavorite() {
    viewModelScope.launch {
      event?.let {
        when (val state = favoriteEventState(it)) {
          is Success -> {
            val message = checkFavorite(it.id)
              ?.let { R.string.msg_ok_add_fav }
              ?: R.string.msg_ok_remove_fav
            _favoriteMessage.postValue(message)
          }
          is Error -> _favoriteMessage.postValue(state.message)
        }
      } ?: _favoriteMessage.postValue(R.string.msg_please_wait)
    }
  }

  // -- private --
  private suspend fun checkFavorite(id: Long): Event? {
    val local = eventRepo.getFavorite(id)
    val result = if (local is Success) local.data else null
    isInFavorite.postValue(result != null)
    return result
  }

  private suspend fun favoriteEventState(it: Event): State<Boolean> {
    return when (isInFavorite.value) {
      true -> eventRepo.removeFavorite(it.id)
      else -> eventRepo.addFavorite(it)
    }
  }
}