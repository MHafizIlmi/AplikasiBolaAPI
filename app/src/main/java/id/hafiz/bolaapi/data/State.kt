/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.data

sealed class State<out R> {
  class Success<out T>(val data: T) : State<T>()
  class Error(val message: Int) : State<Nothing>()
  object Loading : State<Nothing>()
  object Empty : State<Nothing>()
}