/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.league

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.ext.observe
import id.hafiz.bolaapi.ext.viewModel
import id.hafiz.bolaapi.util.GlideApp
import id.hafiz.bolaapi.view.StateView
import id.hafiz.bolaapi.view.stateView
import org.jetbrains.anko.padding
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip

class LeagueFragment : Fragment() {

  private val model: LeagueViewModel by viewModel()

  private lateinit var adapter: LeagueAdapter
  private lateinit var stateView: StateView<List<League>>

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = UI {
    stateView = stateView {
      id = R.id.leagueListContainer
      setRecyclerView {
        id = R.id.leagueGrid
        clipToPadding = false
        padding = dip(8)
      }
    }
  }.view

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupStateView()
    subscribeObservers()
  }

  private fun setupStateView() {
    val columnSpan = resources.getInteger(R.integer.list_league_col_span)
    adapter = LeagueAdapter(GlideApp.with(this))
    stateView.setup(adapter, GridLayoutManager(context, columnSpan))
  }

  private fun subscribeObservers() {
    observe(model.leagues) { stateView.handleState(it, adapter::submitList) }
  }
}
