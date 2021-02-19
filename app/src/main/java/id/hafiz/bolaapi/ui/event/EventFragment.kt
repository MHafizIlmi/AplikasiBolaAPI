/*
 * hafiz on 12/5/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
import com.google.android.material.snackbar.Snackbar
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.ext.observe
import id.hafiz.bolaapi.ext.viewModel
import id.hafiz.bolaapi.util.GlideApp
import id.hafiz.bolaapi.view.StateView
import id.hafiz.bolaapi.view.navigationUpEnable
import id.hafiz.bolaapi.view.searchMenuEnable
import id.hafiz.bolaapi.view.stateView
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI

class EventFragment : Fragment() {

  private val args: EventFragmentArgs by navArgs()
  private val model: EventViewModel by viewModel()

  private lateinit var adapter: EventAdapter
  private lateinit var stateView: StateView<List<Event>>
  private lateinit var snackBar: Snackbar

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = UI {
    coordinatorLayout {
      id = R.id.eventFragment
      appBarLayout {
        id = R.id.eventToolbarContainer
        lparams(matchParent)
        toolbar {
          id = R.id.eventToolbar
          title = args.desc
          navigationUpEnable()
          searchMenuEnable()
        }.lparams(matchParent) {
          scrollFlags = SCROLL_FLAG_SNAP
        }
      }
      stateView = stateView<List<Event>> {
        id = R.id.eventListContainer
        setRecyclerView {
          id = R.id.eventList
        }
      }.lparams(matchParent, matchParent) {
        behavior = AppBarLayout.ScrollingViewBehavior()
      }
    }
  }.view

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupSnackBar(view)
    setupStateView()
    subscribeObservers()
    model.loadEvents(args.leagueId, args.eventType)
  }

  private fun setupStateView() {
    adapter = EventAdapter(GlideApp.with(this))
    stateView.setup(adapter, LinearLayoutManager(context))
  }

  private fun setupSnackBar(view: View) {
    snackBar = Snackbar.make(
      view, R.string.msg_long_wait,
      Snackbar.LENGTH_INDEFINITE
    ).setAction(R.string.dismiss) {}
  }

  private fun subscribeObservers() {
    observe(model.eventsState) { stateView.handleState(it, adapter::submitList) }
    observe(model.notifier) { if (it) snackBar.show() else snackBar.dismiss() }
  }
}