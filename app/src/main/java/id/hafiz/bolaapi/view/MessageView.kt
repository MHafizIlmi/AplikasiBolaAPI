/*
 * hafiz on 12/15/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.jetbrains.anko.*

class MessageView(context: Context) : _LinearLayout(context) {

  private val messageImage: ImageView
  private val messageText: TextView

  init {
    orientation = VERTICAL
    messageImage = imageView()
      .lparams(matchParent, dip(92))
    messageText = textView {
      textSize = 24f
      typeface = Typeface.DEFAULT_BOLD
      textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }.lparams(matchParent)
  }

  fun setMessage(@DrawableRes image: Int, @StringRes text: Int) {
    messageImage.setImageResource(image)
    messageText.setText(text)
  }
}