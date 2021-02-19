/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.hafiz.bolaapi.data.source.repository.EventRepository
import id.hafiz.bolaapi.data.source.repository.LeagueRepository
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import id.hafiz.bolaapi.ui.event.EventViewModel
import id.hafiz.bolaapi.ui.eventdetail.EventDetailViewModel
import id.hafiz.bolaapi.ui.favorite.FavoriteViewModel
import id.hafiz.bolaapi.ui.league.LeagueViewModel
import id.hafiz.bolaapi.ui.leaguedetail.LeagueDetailViewModel
import id.hafiz.bolaapi.ui.search.SearchViewModel
import id.hafiz.bolaapi.ui.team.TeamViewModel
import id.hafiz.bolaapi.ui.teamdetail.TeamDetailViewModel

class ViewModelFactory(repositories: Map<String, Any>) : ViewModelProvider.NewInstanceFactory() {

  private val leagueRepository: LeagueRepository by repositories
  private val eventRepository: EventRepository by repositories
  private val teamRepository: TeamRepository by repositories

  private val models = mapOf(
    LeagueDetailViewModel::class.java to LeagueDetailViewModel(
      leagueRepository, eventRepository, teamRepository
    ),
    LeagueViewModel::class.java to LeagueViewModel(leagueRepository),
    EventViewModel::class.java to EventViewModel(eventRepository),
    EventDetailViewModel::class.java to EventDetailViewModel(eventRepository),
    SearchViewModel::class.java to SearchViewModel(eventRepository, teamRepository),
    FavoriteViewModel::class.java to FavoriteViewModel(eventRepository, teamRepository),
    TeamViewModel::class.java to TeamViewModel(teamRepository),
    TeamDetailViewModel::class.java to TeamDetailViewModel(teamRepository)
  )

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(model: Class<T>): T {
    return models[model] as T? ?: throw IllegalArgumentException("Unknown ViewModel class: $model")
  }
}