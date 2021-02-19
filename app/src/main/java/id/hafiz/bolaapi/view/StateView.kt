/*
 * hafiz on 12/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class StateView<T>(context: Context) : _FrameLayout(context) {

  private val recyclerView = recyclerView()
  private val messageView = messageView()
  private val loadingView = progressBar()

  init {
    applyRecursively {
      when (it) {
        is RecyclerView, is FrameLayout ->
          it.lparams(matchParent, matchParent)
        is ProgressBar, is MessageView ->
          it.apply {
            lparams(matchParent) {
              gravity = Gravity.CENTER
              padding = dip(16)
            }
          }
      }
    }
  }

  fun setRecyclerView(init: RecyclerView.() -> Unit) {
    init(recyclerView)
  }

  fun setup(adapter: Adapter<*>, manager: LayoutManager) {
    recyclerView.adapter = adapter
    recyclerView.layoutManager = manager
  }

  fun handleState(state: State<T>, onResult: (T?) -> Unit) {
    when (state) {
      is State.Loading -> isLoading(true)
      is State.Error -> setMessage(R.drawable.ic_error, state.message)
      is State.Empty -> {
        setMessage(R.drawable.ic_empty, R.string.msg_empty_result)
        onResult(null)
      }
      is State.Success -> {
        isLoading(false)
        onResult(state.data)
      }
    }
  }

  private fun isLoading(constraint: Boolean) {
    loadingView.isVisible = constraint
    messageView.isVisible = false
  }

  private fun setMessage(image: Int, text: Int) {
    loadingView.isVisible = false
    messageView.isVisible = true
    messageView.setMessage(image, text)
  }
}