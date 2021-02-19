/*
 * hafiz on 12/5/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.ext.toStrings
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView

class TeamLineupView(context: Context) : _CardView(context) {

  private lateinit var titleView: TextView
  private val playerContainer: LinearLayout

  init {
    id = R.id.teamLineupView
    lparams(matchParent) {
      horizontalMargin = dip(16)
      verticalMargin = dip(8)
    }
    playerContainer = verticalLayout {
      lparams(matchParent) { padding = dip(8) }
      titleView = textView {
        typeface = Typeface.DEFAULT_BOLD
        textSize = 16f
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
      }.lparams(matchParent)
    }
  }

  fun setTitle(title: String) {
    titleView.text = title
  }

  fun setColor(colorRes: Int) {
    setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
  }

  fun addLine(players: String?) {
    playerContainer.addView(lineupView(players.toStrings(";")))
  }

  private fun View.lineupView(values: List<String>?): View {
    return if (values.isNullOrEmpty()) {
      TextView(context).apply {
        lparams(matchParent)
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        text = "-"
      }
    } else object : _LinearLayout(context) {
      init {
        lparams(matchParent) {
          weightSum = values.size.toFloat()
        }
        orientation = HORIZONTAL
        values.forEach {
          textView(it) {
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_position, 0, 0)
          }.lparams(0) {
            margin = dip(4)
            weight = 1f
          }
        }
      }
    }
  }
}