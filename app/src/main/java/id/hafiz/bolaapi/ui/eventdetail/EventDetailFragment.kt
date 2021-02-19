/*
 * hafiz on 12/4/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.eventdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.ext.observe
import id.hafiz.bolaapi.ext.viewModel
import id.hafiz.bolaapi.util.GlideApp
import id.hafiz.bolaapi.view.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI

class EventDetailFragment : Fragment() {

  private val args: EventDetailFragmentArgs by navArgs()
  private val model: EventDetailViewModel by viewModel()

  private lateinit var adapter: EventDetailAdapter
  private lateinit var stateView: StateView<Entity>
  private lateinit var toolbar: Toolbar

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    state: Bundle?
  ): View? = UI {
    coordinatorLayout {
      id = R.id.eventDetailFragment
      appBarLayout {
        id = R.id.eventDetailToolbarContainer
        lparams(matchParent)
        toolbar = toolbar {
          id = R.id.eventDetailToolbar
          navigationUpEnable()
          favoriteMenuEnable(model::toggleFavorite)
          searchMenuEnable()
        }.lparams(matchParent) {
          scrollFlags = SCROLL_FLAG_SNAP
        }
      }
      stateView = stateView<Entity> {
        id = R.id.eventDetailItemContainer
        setRecyclerView {
          id = R.id.eventDetailItems
        }
      }.lparams(matchParent, matchParent) {
        behavior = AppBarLayout.ScrollingViewBehavior()
      }
    }
  }.view

  override fun onViewCreated(view: View, state: Bundle?) {
    super.onViewCreated(view, state)
    adapter = EventDetailAdapter(GlideApp.with(this))
    stateView.setup(adapter, LinearLayoutManager(context))
    subscribeObservers()
    model.loadEvent(args.eventId)
  }

  private fun subscribeObservers() {
    observe(model.favoriteIcon) { toolbar.menu.findItem(R.id.menuFavorite).setIcon(it) }
    observe(model.favoriteMessage) { if (it != 0) stateView.longSnackbar(it) }
    observe(model.eventState) { state ->
      stateView.handleState(state) {
        if (it is Event) {
          toolbar.title = "${it.homeName} VS ${it.awayName}"
        }
        adapter.submitList(listOf(it))
      }
    }
  }
}