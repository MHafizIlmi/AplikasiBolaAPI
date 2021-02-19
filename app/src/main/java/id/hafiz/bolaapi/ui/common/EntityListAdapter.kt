/*
 * hafiz on 12/7/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.common

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.ui.common.EntityListAdapter.EntityViewHolder
import id.hafiz.bolaapi.view.base.EntityView

abstract class EntityListAdapter : ListAdapter<Entity, EntityViewHolder<View>>(EntityDiffCallback) {

  final override fun onBindViewHolder(holder: EntityViewHolder<View>, position: Int) {
    bindItem(holder, getItem(position))
  }

  class EntityViewHolder<out T : View>(val view: T) : RecyclerView.ViewHolder(view)

  private object EntityDiffCallback : DiffUtil.ItemCallback<Entity>() {
    override fun areItemsTheSame(old: Entity, new: Entity) = old.id == new.id
    override fun areContentsTheSame(old: Entity, new: Entity) = old.sameWith(new)
  }

  private inline fun <E, reified V : EntityView<E>> bindItem(holder: EntityViewHolder<*>, item: E) {
    if (holder.view is V) holder.view.bind(item)
  }
}