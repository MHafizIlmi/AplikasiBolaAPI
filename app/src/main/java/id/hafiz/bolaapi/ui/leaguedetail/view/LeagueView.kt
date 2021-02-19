package id.hafiz.bolaapi.ui.leaguedetail.view

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.State
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.util.usePlaceholder
import id.hafiz.bolaapi.view.ClusterView
import id.hafiz.bolaapi.view.MessageView
import id.hafiz.bolaapi.view.clusterView
import id.hafiz.bolaapi.view.messageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

class LeagueView(context: Context) : _RelativeLayout(context) {

  private val badge: ImageView
  private val title: TextView
  private val clusterDesc: ClusterView
  private val messageView: MessageView
  private val loadingView: ProgressBar

  private lateinit var description: TextView
  private lateinit var glide: GlideRequests
  private lateinit var toolbar: Toolbar

  init {
    id = R.id.leagueView
    // header background
    view {
      backgroundResource = R.color.colorPrimary
    }.lparams(
      matchParent,
      matchParent
    ) {
      alignParentTop()
      sameBottom(R.id.itemBadge)
      bottomMargin = dip(95)
    }

    // badge
    badge = imageView {
      id = R.id.itemBadge
    }.lparams(
      height = dip(200),
      width = matchParent
    ) {
      centerHorizontally()
      topMargin = dip(16)
      bottomMargin = dip(8)
    }

    // title
    title = textView {
      id = R.id.itemTitle
      textSize = 24f
      typeface = Typeface.DEFAULT_BOLD
      textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }.lparams(matchParent) {
      below(R.id.itemBadge)
      horizontalMargin = dip(16)
      bottomPadding = dip(8)
    }

    // TODO: social & web links

    clusterDesc = clusterView {
      description = textView {
        maxLines = 4
        ellipsize = TextUtils.TruncateAt.END
        horizontalPadding = dip(16)
      }.lparams(matchParent)
    }.lparams(matchParent) {
      below(R.id.itemTitle)
    }

    loadingView = progressBar {
      lparams(matchParent) {
        gravity = Gravity.CENTER
        padding = dip(16)
      }
    }
    messageView = messageView {
      lparams(matchParent) {
        gravity = Gravity.CENTER
        padding = dip(16)
      }
    }
  }

  fun setup(glide: GlideRequests, toolbar: Toolbar) {
    this.glide = glide
    this.toolbar = toolbar
  }

  fun setLeagueState(state: State<League>) {
    when (state) {
      is State.Loading -> isLoading(true)
      is State.Error -> setMessage(R.drawable.ic_error, state.message)
      is State.Empty -> setMessage(R.drawable.ic_empty, R.string.msg_empty_result)
      is State.Success -> {
        isLoading(false)
        setLeague(state.data)
      }
    }
  }

  private fun setLeague(e: League) {
    if (::glide.isInitialized) {
      glide.load(e.badgeUrl)
        .usePlaceholder(context)
        .into(badge)
    }

    toolbar.title = e.name
    title.text = e.name
    description.text = e.description
    clusterDesc.setTitle("About this league")
    clusterDesc.setOnClickListener {
      context.alert(e.description, e.name) {
        yesButton { it.dismiss() }
        show()
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

  companion object {
    fun ViewManager.leagueView() = leagueView { }
    inline fun ViewManager.leagueView(init: LeagueView.() -> Unit): LeagueView {
      return ankoView({ LeagueView(it) }, 0, init)
    }
  }
}