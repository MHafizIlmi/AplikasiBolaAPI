/*
 * hafiz on 12/5/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.eventdetail

import android.view.View
import android.view.ViewGroup
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.withHolder

class EventDetailAdapter(private val glide: GlideRequests) : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder<View> {
    val detailView = EventDetailView(parent.context)
    detailView.eventBadge.setGlide(glide)
    return detailView.withHolder()
  }
}