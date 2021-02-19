/*
 * hafiz on 12/10/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.ui.common.EntityListAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.indefiniteSnackbar
import org.jetbrains.anko.recyclerview.v7.recyclerView

class ListingView(context: Context) : _FrameLayout(context) {

  private var itemCount = 0

  private lateinit var adapter: EntityListAdapter

  private val recyclerView: RecyclerView
  private val emptyView: LinearLayout
  private val loadingBar = createSnackLoading()
  private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {

    override fun onItemRangeInserted(positionStart: Int, insertCount: Int) {
      super.onItemRangeInserted(positionStart, insertCount)
      itemCount += insertCount
      setEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, removeCount: Int) {
      super.onItemRangeRemoved(positionStart, removeCount)
      itemCount -= removeCount
      setEmpty()
    }
  }

  init {
    lparams(matchParent, matchParent)
    recyclerView = recyclerView()
      .lparams(matchParent, matchParent)
    emptyView = verticalLayout {
      imageView(R.drawable.ic_empty)
        .lparams(matchParent, dip(92))
      textView(R.string.msg_empty_result) {
        textSize = 24f
        typeface = Typeface.DEFAULT_BOLD
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
      }.lparams(matchParent)
    }.lparams(matchParent) {
      gravity = Gravity.CENTER
      margin = dip(16)
    }
  }

  fun setRecyclerView(init: RecyclerView.() -> Unit) {
    init(recyclerView)
  }

  fun setup(entityAdapter: EntityListAdapter, manager: RecyclerView.LayoutManager) {
    adapter = entityAdapter
    adapter.registerAdapterDataObserver(adapterDataObserver)
    recyclerView.layoutManager = manager
    recyclerView.adapter = adapter
  }

  fun isLoading(constraint: Boolean) {
    if (constraint) loadingBar.show() else {
      Handler().postDelayed(loadingBar::dismiss, 500)
    }
  }

  fun setError(message: Int) {
    indefiniteSnackbar(message, android.R.string.ok) {}
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    if (::adapter.isInitialized) {
      adapter.unregisterAdapterDataObserver(adapterDataObserver)
    }
  }

  private fun setEmpty() {
    emptyView.isVisible = itemCount == 0
  }

  private fun createSnackLoading(): Snackbar {
    val snackBar = Snackbar.make(this, R.string.msg_loading, Snackbar.LENGTH_INDEFINITE)
    val layout = snackBar.view as Snackbar.SnackbarLayout
    val customLayout = _FrameLayout(context).apply {
      progressBar().lparams(dip(36), matchParent) {
        gravity = Gravity.END
      }
    }
    layout.addView(customLayout)
    return snackBar
  }
}