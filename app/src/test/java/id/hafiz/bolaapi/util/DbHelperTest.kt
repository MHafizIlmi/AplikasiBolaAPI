/*
 * hafiz on 11/18/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.util

import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.UNIQUE
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class DbHelperTest {

  data class FakeTeam(
    @PrimaryKey
    val id: Long,
    val name: String,
    val year: Int?,
    val badge: String?
  )

  @Test
  fun createColumns() {
    val result = createColumns<FakeTeam>()

    val columns = arrayOf(
      "id" to INTEGER + PRIMARY_KEY + UNIQUE,
      "name" to TEXT,
      "year" to INTEGER,
      "badge" to TEXT
    )

    val resultString = result.map { "${it.first} ${it.second.render()}" }
    val columnsString = columns.map { "${it.first} ${it.second.render()}" }

    assertEquals(resultString, columnsString)
  }

  @Test
  fun contentValueOf() {
    val team = FakeTeam(133612, "Man United", 1878, "/badge.png")
    val result = contentValueOf(team)

    val content = arrayOf(
      "id" to team.id,
      "name" to team.name,
      "year" to team.year,
      "badge" to team.badge
    )

    // contentValueOf using KClass#memberProperties
    // that sorted by it's name
    content.sortBy { it.first }

    assertArrayEquals(result, content)
  }
}