/*
 * hafiz on 11/4/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.ext.dashable
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko.*

class EventBadgeView(context: Context) : _RelativeLayout(context), EntityView<Event> {

  private val homeBadge: ImageView
  private val awayBadge: ImageView
  private val homeTitle: TextView
  private val awayTitle: TextView

  private lateinit var score: TextView
  private lateinit var playTime: TextView
  private lateinit var glide: GlideRequests

  constructor(
    glide: GlideRequests,
    parent: ViewGroup
  ) : this(parent.context) {
    this.glide = glide
  }

  init {
    id = R.id.eventBadgeView
    background = context.selectableItemBackground()
    lparams(matchParent) {
      verticalPadding = dip(16)
    }

    // home badge
    homeBadge = imageView {
      id = R.id.badgeHome
      adjustViewBounds = true
    }.lparams(height = dip(96)) {
      alignParentStart()
      horizontalMargin = dip(24)
      startOf(R.id.infoContainer)
    }

    // home name
    homeTitle = textView {
      id = R.id.titleHome
      textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }.lparams {
      alignStart(R.id.badgeHome)
      alignEnd(R.id.badgeHome)
      below(R.id.badgeHome)
      topMargin = dip(8)
    }

    // score & date container
    verticalLayout {
      id = R.id.infoContainer
      score = textView {
        id = R.id.scoreText
        textSize = 36f
        typeface = Typeface.DEFAULT_BOLD
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
      }
      playTime = textView {
        id = R.id.playTimeText
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
      }
    }.lparams {
      centerInParent()
    }

    // away badge
    awayBadge = imageView {
      id = R.id.badgeAway
      adjustViewBounds = true
    }.lparams(height = dip(96)) {
      alignParentEnd()
      horizontalMargin = dip(24)
      endOf(R.id.infoContainer)
    }

    // away name
    awayTitle = textView {
      id = R.id.titleAway
      textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }.lparams {
      alignStart(R.id.badgeAway)
      alignEnd(R.id.badgeAway)
      below(R.id.badgeAway)
      topMargin = dip(8)
    }
  }

  fun setGlide(glide: GlideRequests) {
    this.glide = glide
  }

  override fun bind(e: Event): Unit = with(e) {
    val bothScore = "${homeScore.dashable()} : ${awayScore.dashable()}"
    val dateTime = "${time.dashable()}\n${date.dashable()}"
    homeTitle.text = homeName
    awayTitle.text = awayName
    score.text = bothScore
    playTime.text = dateTime
    if (::glide.isInitialized) {
      homeBadgePath?.let { glide.load(it).into(homeBadge) }
      awayBadgePath?.let { glide.load(it).into(awayBadge) }
    }
  }
}