/*
 * hafiz on 12/4/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import id.hafiz.bolaapi.R
import org.jetbrains.anko.*

class ClusterView(context: Context) : _LinearLayout(context) {

  private val header: FrameLayout

  private lateinit var headerTitle: TextView
  private lateinit var arrowImage: ImageView

  init {
    lparams(matchParent) {
      orientation = VERTICAL
      bottomPadding = dip(16)
    }
    header = frameLayout {
      lparams(matchParent) {
        horizontalPadding = dip(16)
      }
      linearLayout {
        lparams(matchParent) {
          orientation = HORIZONTAL
          gravity = Gravity.CENTER
        }
        // head title
        headerTitle = textView {
          textSize = 18f
          typeface = Typeface.DEFAULT_BOLD
        }.lparams(width = 0, weight = 1F) {
          verticalPadding = dip(8)
        }
        // head arrow
        arrowImage = imageView(R.drawable.ic_arrow_forward) {
          visibility = View.GONE
        }.lparams {
          marginStart = dip(8)
        }
      }
    }
    // children
  }

  fun setTitle(title: String) {
    headerTitle.text = title
  }

  override fun setOnClickListener(listener: OnClickListener?) {
    listener?.let {
      arrowImage.isVisible = true
      header.setOnClickListener(it)
      header.applyClickEffect()
    }
  }
}