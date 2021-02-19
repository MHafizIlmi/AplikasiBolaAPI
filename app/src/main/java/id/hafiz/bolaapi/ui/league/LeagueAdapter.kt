/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.league

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import id.hafiz.bolaapi.ui.home.HomeFragmentDirections
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.withHolder

class LeagueAdapter(private val glide: GlideRequests) : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder<View> {
    return LeagueItemView(glide, parent).withHolder(::navigateToDetail)
  }

  private fun navigateToDetail(view: View, position: Int) {
    getItem(position)?.let {
      val action = HomeFragmentDirections.actionToLeagueDetail(it.id)
      view.findNavController().navigate(action)
    }
  }
}