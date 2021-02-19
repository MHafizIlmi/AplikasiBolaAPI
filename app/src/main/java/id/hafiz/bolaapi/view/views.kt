/*
 * hafiz on 12/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewManager
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.android.material.chip.ChipGroup
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.ui.common.EntityListAdapter.EntityViewHolder
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.custom.ankoView


fun Context.selectableItemBackground(): Drawable? {
  val typedValue = TypedValue()
  theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
  return ContextCompat.getDrawable(this, typedValue.resourceId)
}

fun FrameLayout.applyClickEffect() {
  foreground = context.selectableItemBackground()
  isClickable = true
  isFocusable = true
}

fun SearchView.onQuerySubmit(action: (String?) -> Boolean) {
  setOnQueryTextListener(object : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?) = action(query)
    override fun onQueryTextChange(newText: String?) = false
  })
}

fun Toolbar.navigationUpEnable() {
  setNavigationOnClickListener { it.findNavController().navigateUp() }
  navigationIconResource = R.drawable.ic_arrow_back
  contentInsetStartWithNavigation = 0
}

fun Toolbar.favoriteMenuEnable(onClick: () -> Unit) {
  menu.add(0, R.id.menuFavorite, 0, R.string.menu_favorite)
    .setIcon(R.drawable.ic_favorite_removed)
    .setOnMenuItemClickListener { onClick(); true }
    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
}

fun Toolbar.setFavoriteMenuIcon(iconRes: Int) {
  menu.findItem(R.id.menuFavorite).setIcon(iconRes)
}

fun Toolbar.searchMenuEnable() {
  menu.add(0, R.id.menuSearch, 0, R.string.menu_search)
    .setIcon(R.drawable.ic_search)
    .setOnMenuItemClickListener { findNavController().navigate(R.id.action_to_search); true }
    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
}

fun ViewManager.messageView() = messageView {}
inline fun ViewManager.messageView(init: MessageView.() -> Unit): MessageView {
  return ankoView({ MessageView(it) }, 0, init)
}

inline fun <T> ViewManager.stateView(init: StateView<T>.() -> Unit): StateView<T> {
  return ankoView({ StateView(it) }, 0, init)
}

fun <T : Entity> ViewManager.clusterListView(): ClusterListView<T> {
  return ankoView({ ClusterListView(it) }, 0) {}
}

inline fun ViewManager.clusterView(init: ClusterView.() -> Unit): ClusterView {
  return ankoView({ ClusterView(it) }, 0, init)
}

fun ViewManager.eventBadgeView() = eventBadgeView { }
inline fun ViewManager.eventBadgeView(init: EventBadgeView.() -> Unit): EventBadgeView {
  return ankoView({ EventBadgeView(it) }, 0, init)
}

fun ViewManager.coupleTextView() = coupleTextView { }
inline fun ViewManager.coupleTextView(init: CoupleTextView.() -> Unit): CoupleTextView {
  return ankoView({ CoupleTextView(it) }, 0, init)
}

fun ViewManager.teamLineupView() = teamLineupView { }
inline fun ViewManager.teamLineupView(init: TeamLineupView.() -> Unit): TeamLineupView {
  return ankoView({ TeamLineupView(it) }, 0, init)
}

fun ViewManager.listingView() = listingView { }
inline fun ViewManager.listingView(init: ListingView.() -> Unit): ListingView {
  return ankoView({ ListingView(it) }, 0, init)
}

inline fun ViewManager.carouselView(init: CarouselView.() -> Unit): CarouselView {
  return ankoView({ CarouselView(it) }, 0, init)
}

fun ViewManager.chipGroup(): ChipGroup {
  return ankoView({ ChipGroup(it) }, 0) {}
}

fun <T : View> T.withHolder(onItemClick: ((View, Int) -> Unit)? = null): EntityViewHolder<T> {
  return EntityViewHolder(this).apply {
    onItemClick?.also { itemClick ->
      itemView.setOnClickListener {
        itemClick(it, adapterPosition)
      }
    }
  }
}
