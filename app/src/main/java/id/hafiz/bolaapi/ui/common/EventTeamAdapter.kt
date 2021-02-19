/*
 * hafiz on 12/6/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.common

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import id.hafiz.bolaapi.MainNavGraphDirections
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.ui.team.TeamView
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.EventBadgeView
import id.hafiz.bolaapi.view.withHolder

class EventTeamAdapter(private val glide: GlideRequests) : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
    R.id.eventBadgeView -> EventBadgeView(glide, parent).withHolder(::navigateToEventDetail)
    R.id.teamView -> TeamView(glide, parent).withHolder(::navigateToTeamDetail)
    else -> throw IllegalArgumentException("Unknown view type: $viewType")
  }

  override fun getItemViewType(position: Int): Int {
    return when (getItem(position)) {
      is Event -> R.id.eventBadgeView
      is Team -> R.id.teamView
      else -> throw IllegalArgumentException("Item is not subclass of Entity")
    }
  }

  private fun navigateToEventDetail(view: View, position: Int) {
    val item = getItem(position)
    val action = MainNavGraphDirections.actionToEventDetail(item.id)
    view.findNavController().navigate(action)
  }

  private fun navigateToTeamDetail(view: View, position: Int) {
    val item = getItem(position)
    val action = MainNavGraphDirections.actionToTeamDetail(item.id)
    view.findNavController().navigate(action)
  }
}