package id.hafiz.bolaapi.ui.teamdetail

import android.view.View
import android.view.ViewGroup
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.withHolder

class TeamDetailAdapter(private val glide: GlideRequests) : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder<View> {
    return TeamDetailView(glide, parent).withHolder()
  }

  fun setTeam(team: Team?) {
    if (team == null) return
    val list = currentList
      .toMutableList().apply { add(0, team) }
    submitList(list)
  }
}
