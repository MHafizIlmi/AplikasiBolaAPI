/*
 * hafiz on 12/2/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.leaguedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.data.source.repository.EventRepository.EventType
import id.hafiz.bolaapi.ext.observe
import id.hafiz.bolaapi.ext.viewModel
import id.hafiz.bolaapi.ui.event.EventAdapter
import id.hafiz.bolaapi.ui.leaguedetail.adapter.ClassementAdapter
import id.hafiz.bolaapi.ui.leaguedetail.view.LeagueView
import id.hafiz.bolaapi.ui.leaguedetail.view.LeagueView.Companion.leagueView
import id.hafiz.bolaapi.ui.team.TeamAdapter
import id.hafiz.bolaapi.util.GlideApp
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.ClusterListView
import id.hafiz.bolaapi.view.clusterListView
import id.hafiz.bolaapi.view.navigationUpEnable
import id.hafiz.bolaapi.view.searchMenuEnable
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView
import org.jetbrains.anko.verticalLayout

class LeagueDetailFragment : Fragment() {

  private val args: LeagueDetailFragmentArgs by navArgs()
  private val model: LeagueDetailViewModel by viewModel()

  private lateinit var glide: GlideRequests
  private lateinit var toolbar: Toolbar

  private lateinit var leagueView: LeagueView
  private lateinit var teamsView: ClusterListView<Team>
  private lateinit var pastEventsView: ClusterListView<Event>
  private lateinit var nextEventsView: ClusterListView<Event>
  private lateinit var standingsView: ClusterListView<Standing>

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    state: Bundle?
  ): View? = UI {
    coordinatorLayout {
      id = R.id.leagueDetailFragment
      lparams(matchParent, matchParent)
      appBarLayout {
        id = R.id.leagueDetailToolbarContainer
        lparams(matchParent)
        isLiftOnScroll = true
        toolbar = toolbar {
          id = R.id.leagueDetailToolbar
          navigationUpEnable()
          searchMenuEnable()
        }.lparams(matchParent) {
          scrollFlags = SCROLL_FLAG_SNAP
        }
      }

      nestedScrollView {
        id = R.id.leagueDetailItemContainer
        verticalLayout {
          id = R.id.leagueDetailItems
          leagueView = leagueView()
          teamsView = clusterListView()
          pastEventsView = clusterListView()
          nextEventsView = clusterListView()
          standingsView = clusterListView()
        }
      }.lparams(matchParent) {
        behavior = AppBarLayout.ScrollingViewBehavior()
      }
    }
  }.view

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    glide = GlideApp.with(this)
    setupViews()
    subscribeObservers()
    model.loadLeague(args.leagueId)
  }

  private fun setupViews() {
    leagueView.setup(glide, toolbar)
    teamsView.setup("All teams", TeamAdapter(glide, true)) { goToTeams(it) }
    pastEventsView.setup("Latest results", EventAdapter(glide)) { goToEvents(it, EventType.PAST) }
    nextEventsView.setup("Upcoming events", EventAdapter(glide)) { goToEvents(it, EventType.NEXT) }
    standingsView.setup("Classement table", ClassementAdapter(), true)
  }

  private fun subscribeObservers() {
    observe(model.leagueState, leagueView::setLeagueState)
    observe(model.teamState, teamsView::setState)
    observe(model.pastEventState, pastEventsView::setState)
    observe(model.nextEventState, nextEventsView::setState)
    observe(model.standingState, standingsView::setState)
  }

  private fun goToEvents(title: String, type: EventType) {
    val desc = "$title - ${toolbar.title}"
    val action = LeagueDetailFragmentDirections.actionToEvent(args.leagueId, type, desc)
    findNavController().navigate(action)
  }

  private fun goToTeams(title: String) {
    val desc = "$title - ${toolbar.title}"
    val action = LeagueDetailFragmentDirections.actionToTeam(args.leagueId, desc)
    findNavController().navigate(action)
  }
}