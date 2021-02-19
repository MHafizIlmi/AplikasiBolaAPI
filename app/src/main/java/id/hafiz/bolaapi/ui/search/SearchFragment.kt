/*
 * hafiz on 12/6/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.ext.observe
import id.hafiz.bolaapi.ext.viewModel
import id.hafiz.bolaapi.ui.common.EventTeamAdapter
import id.hafiz.bolaapi.util.GlideApp
import id.hafiz.bolaapi.view.ListingView
import id.hafiz.bolaapi.view.listingView
import id.hafiz.bolaapi.view.onQuerySubmit
import org.jetbrains.anko.appcompat.v7.searchView
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout

class SearchFragment : Fragment() {

  private val model: SearchViewModel by viewModel()

  private lateinit var adapter: EventTeamAdapter
  private lateinit var listingView: ListingView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = UI {
    verticalLayout {
      id = R.id.searchFragment
      lparams(matchParent, matchParent)
      cardView {
        id = R.id.searchView
        searchView {
          id = R.id.searchInput
          setIconifiedByDefault(false)
          queryHint = getString(R.string.title_search_hint)
          requestFocus()
          setQuery(model.currentQuery, false)
          onQuerySubmit { it?.let { query -> model.search(query) }; true }
        }.lparams(matchParent)
      }.lparams(matchParent) {
        horizontalMargin = dip(16)
        topMargin = dip(16)
      }
      listingView = listingView {
        id = R.id.searchResultContainer
        setRecyclerView {
          id = R.id.searchResults
        }
      }
    }
  }.view

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupStateView()
    subscribeObservers()
  }

  private fun setupStateView() {
    adapter = EventTeamAdapter(GlideApp.with(this))
    listingView.setup(adapter, LinearLayoutManager(context))
  }

  private fun subscribeObservers() {
    observe(model.result, adapter::submitList)
    observe(model.loading, listingView::isLoading)
    observe(model.error, listingView::setError)
  }
}