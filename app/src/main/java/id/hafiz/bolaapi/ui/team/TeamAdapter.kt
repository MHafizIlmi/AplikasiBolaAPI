/*
 * hafiz on 12/9/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.team

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import id.hafiz.bolaapi.MainNavGraphDirections
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.TeamSimpleView
import id.hafiz.bolaapi.view.withHolder

class TeamAdapter(
  private val glide: GlideRequests,
  private val simple: Boolean = false
) : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder<View> {
    return if (simple) TeamSimpleView(glide, parent).withHolder(::navigateToDetail)
    else TeamView(glide, parent).withHolder(::navigateToDetail)
  }

  private fun navigateToDetail(view: View, position: Int) {
    val item = getItem(position)
    val action = MainNavGraphDirections.actionToTeamDetail(item.id)
    view.findNavController().navigate(action)
  }
}