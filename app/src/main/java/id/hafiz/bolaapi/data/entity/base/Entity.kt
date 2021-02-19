/*
 * hafiz on 12/6/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.entity.base

interface Entity {
  val id: Long

  fun sameWith(other: Entity) = this == other
}