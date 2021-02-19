/*
 * hafiz on 12/16/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view

import android.content.Context
import android.widget.ImageView
import android.widget.ViewFlipper
import id.hafiz.bolaapi.util.GlideRequests
import id.hafiz.bolaapi.util.usePlaceholder
import org.jetbrains.anko._FrameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.viewFlipper

class CarouselView(context: Context) : _FrameLayout(context) {

  private val flipper: ViewFlipper

  init {
    lparams(matchParent)
    flipper = viewFlipper {
      setInAnimation(context, android.R.anim.slide_in_left)
      setOutAnimation(context, android.R.anim.slide_out_right)
    }.lparams(matchParent)
  }

  fun flipper(init: ViewFlipper.() -> Unit) {
    init(flipper)
  }

  fun setImages(glide: GlideRequests, vararg imageUrls: String?) {
    imageUrls.forEach {
      if (!it.isNullOrBlank()) {
        val image = createImageView()
        flipper.addView(image)

        glide.load(it)
          .usePlaceholder(context)
          .into(image)
      }
    }
    flipper.startFlipping()
  }

  private fun createImageView(): ImageView {
    return ImageView(context).apply {
      adjustViewBounds = true
      scaleType = ImageView.ScaleType.CENTER_CROP
    }.lparams(matchParent)
  }
}