/*
 * hafiz on 12/5/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TextView
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.ext.dashable
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView
import java.text.SimpleDateFormat
import java.util.*

class EventView(context: Context) : _CardView(context), EntityView<Event> {

  private lateinit var home: TextView
  private lateinit var away: TextView
  private lateinit var info: TextView

  init {
    id = R.id.eventView
    applyClickEffect()
    lparams(dip(256), dip(148))
    useCompatPadding = true
    radius = dip(8).toFloat()
    relativeLayout {
      padding = dip(16)
      backgroundResource = R.drawable.bg_event
      info = textView {
        id = R.id.eventVs
        backgroundResource = R.drawable.bg_event_info
        typeface = Typeface.DEFAULT_BOLD
        gravity = Gravity.CENTER
      }.lparams {
        alignParentBottom()
        centerHorizontally()
        bottomMargin = dip(24)
      }
      home = textView {
        textSize = 18F
        typeface = Typeface.DEFAULT_BOLD
        textColorResource = android.R.color.white
      }.lparams {
        alignParentStart()
        alignParentTop()
        // startOf(R.id.eventVs)
      }
      away = textView {
        textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
        textSize = 18F
        typeface = Typeface.DEFAULT_BOLD
        textColorResource = android.R.color.white
      }.lparams {
        alignParentEnd()
        alignParentBottom()
        // endOf(R.id.eventVs)
      }
    }
  }

  override fun bind(e: Event) {
    home.text = e.homeName
    away.text = e.awayName
    info.text = if (e.homeScore == null && e.awayScore == null) {
      e.dateEvent.toSortDate()
    } else {
      "${e.homeScore} - ${e.awayScore}"
    }
  }

  private fun String?.toSortDate(): String {
    if (this.isNullOrBlank()) return dashable()
    val defaultFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val newFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    return defaultFormat.parse(this)?.let { newFormat.format(it) }.dashable()
  }
}