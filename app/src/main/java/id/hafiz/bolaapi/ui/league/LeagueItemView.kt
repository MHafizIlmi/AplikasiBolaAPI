/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.league

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.League
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.applyClickEffect
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView

class LeagueItemView(context: Context) : _CardView(context), EntityView<League> {

  private lateinit var glide: GlideRequests
  private val badge: ImageView
  private val title: TextView

  constructor(
    glide: GlideRequests,
    parent: ViewGroup
  ) : this(parent.context) {
    this.glide = glide
  }

  init {
    applyClickEffect()
    lparams(
      width = matchParent,
      height = dip(186)
    ) { margin = 8 }

    badge = imageView {
      id = R.id.itemBadge
      scaleType = ScaleType.CENTER_CROP
    }.lparams(
      width = matchParent,
      height = matchParent
    )

    title = textView {
      id = R.id.itemTitle
      maxLines = 1
      textAlignment = TextView.TEXT_ALIGNMENT_CENTER
      backgroundResource = R.drawable.bg_title_gradient
      padding = dip(8)
      textColor = Color.WHITE
      typeface = Typeface.DEFAULT_BOLD
      textSize = 16f // anko textView already interprets value in sp units
    }.lparams(
      gravity = Gravity.CENTER or Gravity.BOTTOM,
      width = matchParent
    )
  }

  override fun bind(e: League) {
    title.text = e.name
    glide.load(e.badgeUrl).into(badge)
  }
}