/*
 * hafiz on 11/12/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.util

import org.jetbrains.anko.db.*
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PrimaryKey

inline fun <reified T : Any> contentValueOf(model: T) =
  T::class.memberProperties
    .map { Pair(it.name, it.get(model)) }
    .toTypedArray()

inline fun <reified T : Any> createColumns(): Array<Pair<String, SqlType>> {
  val constructor = T::class.primaryConstructor
    ?: throw Exception("Primary constructor is required for ${T::class}")
  return constructor.parameters
    .map {
      val name = requireNotNull(it.name) { "Can't get parameter name!" }
      val type = if (it.findAnnotation<PrimaryKey>() != null) {
        sqlTypeMapper(it.type) + PRIMARY_KEY + UNIQUE
      } else {
        sqlTypeMapper(it.type)
      }
      Pair(name, type)
    }.toTypedArray()
}

fun sqlTypeMapper(type: KType) = when (type) {
  // only use INTEGER & TEXT for now
  type<Long>(), type<Int>(), type<Int?>() -> INTEGER
  type<String>(), type<String?>() -> TEXT
  else -> throw IllegalArgumentException("Unsupported type: $type")
}

private inline fun <reified T> type(): KType {
  val nullable = null is T
  return T::class.createType(nullable = nullable)
}