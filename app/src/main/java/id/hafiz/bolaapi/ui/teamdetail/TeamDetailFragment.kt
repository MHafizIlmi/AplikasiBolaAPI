/*
 * hafiz on 12/10/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.teamdetail

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
import id.hafiz.bolaapi.ext.observe
import id.hafiz.bolaapi.ext.viewModel
import id.hafiz.bolaapi.util.GlideApp
import id.hafiz.bolaapi.view.*
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI

class TeamDetailFragment : Fragment() {

  private val args: TeamDetailFragmentArgs by navArgs()
  private val model: TeamDetailViewModel by viewModel()

  private lateinit var adapter: TeamDetailAdapter
  private lateinit var listingView: ListingView
  private lateinit var toolbar: Toolbar

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = UI {
    coordinatorLayout {
      appBarLayout {
        lparams(matchParent)
        toolbar = toolbar {
          titleResource = R.string.title_team
          navigationUpEnable()
          favoriteMenuEnable(model::toggleFavorite)
          searchMenuEnable()
        }.lparams(matchParent) {
          scrollFlags = SCROLL_FLAG_SNAP
        }
      }
      listingView = listingView()
        .lparams(matchParent, matchParent) {
          behavior = AppBarLayout.ScrollingViewBehavior()
        }
    }
  }.view

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = TeamDetailAdapter(GlideApp.with(this))
    listingView.setup(adapter, LinearLayoutManager(context))
    subscribeObservers()
    model.loadTeam(args.teamId)
  }

  private fun subscribeObservers() {
    observe(model.error, listingView::setError)
    observe(model.loading, listingView::isLoading)
    observe(model.favoriteIcon, toolbar::setFavoriteMenuIcon)
    observe(model.favoriteMessage) { if (it != 0) listingView.longSnackbar(it) }
    observe(model.team, adapter::setTeam)
  }
}