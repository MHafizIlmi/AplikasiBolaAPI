/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.util

import android.content.Context
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import id.hafiz.bolaapi.R

@GlideModule
class ImageModule : AppGlideModule() {

  override fun applyOptions(context: Context, builder: GlideBuilder) {
    super.applyOptions(context, builder)
    val options = RequestOptions()
      .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
      .error(R.drawable.ic_error)
    builder
      .setDefaultRequestOptions(options)
      .setLogLevel(Log.ERROR)
  }
}

fun <T> GlideRequest<T>.usePlaceholder(context: Context): GlideRequest<T> {
  val color = ResourcesCompat.getColor(context.resources, R.color.colorAccent, null)
  val loader = CircularProgressDrawable(context).apply {
    strokeWidth = 10f
    centerRadius = 48f
    setColorSchemeColors(color)
    start()
  }
  return placeholder(loader)
}