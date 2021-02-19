/*
 * hafiz on 12/14/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.leaguedetail.view

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TextView
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Standing
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko.*

class ClassementView(context: Context) :
  _LinearLayout(context), EntityView<Standing> {

  companion object {
    const val TYPE_HEADER = 10
    const val TYPE_ODD_ROW = 11
  }

  private val teamName: TextView
  private val played: TextView
  private val goalsFor: TextView
  private val goalsAgainst: TextView
  private val goalsDifference: TextView
  private val win: TextView
  private val draw: TextView
  private val loss: TextView
  private val total: TextView

  init {
    lparams(matchParent) { horizontalMargin = dip(2) }
    orientation = HORIZONTAL
    gravity = Gravity.CENTER
    weightSum = 2F

    teamName = textView("Team")
      .lparams(width = 0, weight = 2F) {
        horizontalMargin = dip(4)
      }

    played = textView("P")
    goalsFor = textView("GF")
    goalsAgainst = textView("GA")
    goalsDifference = textView("GD")
    win = textView("W")
    draw = textView("D")
    loss = textView("L")
    total = textView("T")

    applyRecursively {
      if (it is TextView && it != teamName) {
        it.apply {
          textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }.lparams(dip(24))
      }
    }
  }

  fun withType(type: Int): ClassementView {
    when (type) {
      TYPE_HEADER -> {
        verticalPadding = dip(4)
        backgroundColorResource = R.color.colorPrimary
        applyRecursively {
          if (it is TextView) {
            it.apply {
              textColorResource = android.R.color.white
              typeface = Typeface.DEFAULT_BOLD
            }
          }
        }
      }
      TYPE_ODD_ROW -> backgroundColorResource = R.color.colorListOdd
    }
    return this
  }

  override fun bind(e: Standing) {
    if (e != Standing.EMPTY) {
      teamName.text = e.name
      played.text = e.played.toString()
      goalsFor.text = e.goalsFor.toString()
      goalsAgainst.text = e.goalsAgainst.toString()
      goalsDifference.text = e.goalsDifference.toString()
      win.text = e.win.toString()
      draw.text = e.draw.toString()
      loss.text = e.loss.toString()
      total.text = e.total.toString()
    }
  }
}