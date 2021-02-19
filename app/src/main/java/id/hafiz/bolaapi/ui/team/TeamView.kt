/*
 * hafiz on 12/10/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.team

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.applyClickEffect
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView

class TeamView(context: Context) : _CardView(context), EntityView<Team> {

  private lateinit var glide: GlideRequests
  private lateinit var badge: ImageView
  private lateinit var name: TextView
  private lateinit var desc: TextView

  constructor(
    glide: GlideRequests,
    parent: ViewGroup
  ) : this(parent.context) {
    this.glide = glide
  }

  init {
    id = R.id.teamView
    lparams(matchParent) {
      horizontalMargin = dip(16)
      verticalMargin = dip(4)
    }
    applyClickEffect()
    relativeLayout {
      lparams(matchParent) { padding = dip(8) }

      badge = imageView {
        id = R.id.itemBadge
      }.lparams(dip(126), dip(126)) {
        alignParentTop()
        alignParentStart()
      }

      verticalLayout {
        lparams { padding = dip(8) }
        name = textView {
          textSize = 16F
          typeface = Typeface.DEFAULT_BOLD
        }.lparams(matchParent)

        desc = textView {
          ellipsize = TextUtils.TruncateAt.END
          maxLines = 5
        }.lparams(matchParent)
      }.lparams(matchParent) {
        alignParentTop()
        alignParentEnd()
        endOf(R.id.itemBadge)
        sameBottom(R.id.itemBadge)
      }
    }
  }

  override fun bind(e: Team) {
    val description = "\uD83D\uDDA4 ${e.loved} - ${e.league} - ${e.description}"
    name.text = e.name
    desc.text = description
    if (::glide.isInitialized) {
      glide.load(e.badgePath).into(badge)
    }
  }
}