/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.entity

import com.google.gson.annotations.SerializedName
import id.hafiz.bolaapi.data.entity.base.Entity

data class League(

  @SerializedName("idLeague")
  override val id: Long,

  @SerializedName("strLeague")
  val name: String,

  @SerializedName("strDescriptionEN")
  val description: String = "",

  @SerializedName("strBadge")
  val badgeUrl: String = ""
) : Entity