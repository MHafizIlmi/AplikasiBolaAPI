/*
 * hafiz on 12/11/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.teamdetail

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Team
import id.hafiz.bolaapi.ext.dashable
import id.hafiz.bolaapi.ext.toIconLink
import id.hafiz.bolaapi.ext.toStrings
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.view.CarouselView
import id.hafiz.bolaapi.view.base.EntityView
import id.hafiz.bolaapi.view.carouselView
import id.hafiz.bolaapi.view.chipGroup
import id.hafiz.bolaapi.view.clusterView
import org.jetbrains.anko.*

class TeamDetailView(context: Context) : _LinearLayout(context), EntityView<Team> {

  private val carousel: CarouselView
  private val links: LinearLayout
  private val keywords: ChipGroup

  private lateinit var glide: GlideRequests
  private lateinit var badge: ImageView
  private lateinit var title: TextView
  private lateinit var subtitle: TextView
  private lateinit var desc: TextView

  constructor(
    glide: GlideRequests,
    parent: ViewGroup
  ) : this(parent.context) {
    this.glide = glide
  }

  init {
    id = R.id.teamDetailView
    lparams(matchParent)
    orientation = VERTICAL

    // fanart
    carousel = carouselView {
      flipper {
        flipInterval = 5_000
      }
    }.lparams(matchParent, dip(218))

    // badge + title + year + loved + division
    relativeLayout {
      badge = imageView {
        id = R.id.itemBadge
      }.lparams(dip(126), dip(126)) {
        margin = dip(16)
      }
      verticalLayout {
        title = textView {
          id = R.id.itemTitle
          textSize = 24F
          typeface = Typeface.DEFAULT_BOLD
        }
        subtitle = textView()
      }.lparams(matchParent) {
        verticalMargin = dip(16)
        sameTop(badge)
        endOf(badge)
        alignParentEnd()
      }
    }.lparams(matchParent)

    // links
    links = linearLayout {
      gravity = Gravity.CENTER
    }

    // description
    clusterView {
      desc = textView {
        maxLines = 4
        ellipsize = TextUtils.TruncateAt.END
        horizontalPadding = dip(16)
      }.lparams(matchParent)
      val aboutText = "About this team"
      setTitle(aboutText)
      setOnClickListener {
        context.alert(desc.text, aboutText) {
          yesButton { it.dismiss() }
          show()
        }
      }
    }

    // keywords
    keywords = chipGroup()
      .lparams(matchParent) {
        horizontalMargin = dip(16)
      }

    // jersey

    // teams in same league
  }

  override fun bind(e: Team) {
    val subText =
      "${e.league}\nSince ${e.formedYear}\n\uD83D\uDDA4 ${e.loved}\n${e.division.dashable()}"
    title.text = e.name
    subtitle.text = subText
    desc.text = e.description
    populateLinks(e)
    populateKeywords(e)

    if (::glide.isInitialized) {
      glide.load(e.badgePath).into(badge)
      carousel.setImages(glide, e.fanart, e.fanart2, e.fanart3, e.fanart4)
    }
  }

  private fun populateLinks(e: Team) {
    mapOf(
      "Website" to e.website,
      "Facebook" to e.facebook,
      "YouTube" to e.youtube,
      "Twitter" to e.twitter,
      "Instagram" to e.instagram
    ).forEach {
      it.value?.let { link ->
        val href = ImageView(context).apply {
          setImageResource(it.key.toIconLink())
          setOnClickListener { openLink(link) }
        }.lparams { margin = dip(8) }
        links.addView(href)
      }
    }
  }

  private fun populateKeywords(e: Team) {
    e.keywords.let {
      if (it.isNullOrBlank()) e.name else it
    }.toStrings(",")?.forEach {
      val chip = Chip(context).apply {
        text = it
        isClickable = false
      }
      keywords.addView(chip)
    }
  }

  private fun openLink(link: String) {
    context.toast(link)
  }
}