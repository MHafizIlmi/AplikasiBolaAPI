/*
 * hafiz on 12/7/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.view.base

import id.hafiz.bolaapi.data.entity.base.Entity

interface EntityView<T : Entity> {

  fun bind(e: T)
}

