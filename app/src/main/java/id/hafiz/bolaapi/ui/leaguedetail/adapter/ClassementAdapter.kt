/*
 * hafiz on 12/14/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.leaguedetail.adapter

import android.view.ViewGroup
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import id.hafiz.bolaapi.ui.leaguedetail.view.ClassementView
import id.hafiz.bolaapi.ui.leaguedetail.view.ClassementView.Companion.TYPE_HEADER
import id.hafiz.bolaapi.ui.leaguedetail.view.ClassementView.Companion.TYPE_ODD_ROW
import id.hafiz.bolaapi.view.withHolder

class ClassementAdapter : EntityListAdapter() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ClassementView(parent.context).withType(viewType).withHolder()

  override fun getItemViewType(position: Int) = when {
    position == 0 -> TYPE_HEADER
    position % 2 != 0 -> TYPE_ODD_ROW
    else -> super.getItemViewType(position)
  }

  override fun submitList(list: List<Entity>?) {
    val header = listOf(Standing.EMPTY)
    val data = list?.let { header + it } ?: header
    super.submitList(data)
  }
}