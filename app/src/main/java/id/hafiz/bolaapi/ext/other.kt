/*
 * hafiz on 11/8/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ext

import id.hafiz.bolaapi.R
import java.util.*

private val socialIcons = mapOf(
  "Website" to R.drawable.ic_website,
  "Facebook" to R.drawable.ic_facebook,
  "Twitter" to R.drawable.ic_twitter,
  "Instagram" to R.drawable.ic_instagram,
  "YouTube" to R.drawable.ic_youtube
)

fun Int?.dashable() = if (this == null) "-" else "$this"

fun String?.dashable() = this ?: "-"

fun String?.toStrings(splitter: String) = this
  ?.split(Regex("$splitter[ ]*"))
  ?.dropLastWhile { it.isBlank() }

fun String?.newLine(by: String) =
  if (isNullOrBlank()) "-"
  else this?.replace(Regex("$by[ ]*"), "\n")

fun String.toIconLink() = socialIcons[this] ?: R.drawable.ic_link

fun isBlank(vararg values: String?) = values.all { it.isNullOrBlank() }

fun currentTimeMillis() = Calendar.getInstance(Locale.getDefault()).timeInMillis
