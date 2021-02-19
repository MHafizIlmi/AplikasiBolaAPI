/*
 * hafiz on 12/9/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data.source.local.base

import id.hafiz.bolaapi.data.entity.base.Entity

interface FavoriteDataSource<T : Entity> {

  suspend fun getFavorites(): List<T>

  suspend fun getFavorite(id: Long): T?

  suspend fun saveFavorite(value: T): Long

  suspend fun deleteFavorite(id: Long): Int
}