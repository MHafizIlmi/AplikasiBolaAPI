/*
 * hafiz on 11/2/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.event

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import id.hafiz.bolaapi.MainNavGraphDirections
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.ext.isBlank
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.EventBadgeView
import id.hafiz.bolaapi.view.EventView
import id.hafiz.bolaapi.view.withHolder

class EventAdapter(private val glide: GlideRequests) : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
    R.id.eventBadgeView -> EventBadgeView(glide, parent).withHolder(::onItemClick)
    R.id.eventView -> EventView(parent.context).withHolder(::onItemClick)
    else -> throw IllegalArgumentException("Unknown view type: $viewType")
  }

  override fun getItemViewType(position: Int): Int {
    val entity = getItem(position)
    if (entity !is Event) return 0
    return entity.run {
      if (isBlank(homeBadgePath, awayBadgePath))
        R.id.eventView else R.id.eventBadgeView
    }
  }

  private fun onItemClick(view: View, position: Int) {
    val event = getItem(position)
    val action = MainNavGraphDirections.actionToEventDetail(event.id)
    view.findNavController().navigate(action)
  }
}