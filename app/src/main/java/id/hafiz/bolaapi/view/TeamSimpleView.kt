/*
 * hafiz on 12/9/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko.*

class TeamSimpleView(context: Context) : _LinearLayout(context), EntityView<Team> {

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
    id = R.id.teamSimpleView
    background = context.selectableItemBackground()
    orientation = VERTICAL
    isClickable = true
    isFocusable = true
    badge = imageView {
      adjustViewBounds = true
    }.lparams(dip(126), dip(126)) {
      margin = dip(8)
      bottomPadding = dip(8)
    }
    title = textView {
      textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }
  }

  override fun bind(e: Team) {
    title.text = e.name
    if (::glide.isInitialized) {
      glide.load(e.badgePath).into(badge)
    }
  }
}