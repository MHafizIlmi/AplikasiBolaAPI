package id.hafiz.bolaapi.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.*
import id.hafiz.bolaapi.data.entity.base.Entity
import id.hafiz.bolaapi.data.source.fake.FakeEventRepository
import id.hafiz.bolaapi.data.source.fake.FakeTeamRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify

/**
 * @author Habib hafiz
 */
@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineRule = MainCoroutineRule()

  private val eventRepo = FakeEventRepository()
  private val teamRepo = FakeTeamRepository()
  private val model = FavoriteViewModel(eventRepo, teamRepo)

  @Test
  fun `favoritesTest ALL`() = runBlockingTest {
    val favEvents = eventRepo.getFavorites().succeed
    val favTeams = teamRepo.getFavorites().succeed
    val sourceList = favEvents + favTeams

    val observer: Observer<List<Entity>> = mock()
    model.favorites.observeForever(observer)

    model.loadFavorites()
    val favoriteList = valueOf(model.favorites)
    verify(observer).onChanged(favoriteList)

    favoriteList shouldExactlyEqualsWith sourceList
  }

  @Test
  fun `favoritesTest EVENT`() = runBlockingTest {
    val sourceEvents = eventRepo.getFavorites().succeed

    val observer: Observer<List<Entity>> = mock()
    model.favorites.observeForever(observer)

    model.currentType = FavoriteType.EVENT
    model.loadFavorites()
    val favoriteEvents = valueOf(model.favorites)
    verify(observer).onChanged(favoriteEvents)

    favoriteEvents shouldExactlyEqualsWith sourceEvents
  }

  @Test
  fun `favoritesTest TEAM`() = runBlockingTest {
    val sourceTeams = teamRepo.getFavorites().succeed

    val observer: Observer<List<Entity>> = mock()
    model.favorites.observeForever(observer)

    model.currentType = FavoriteType.TEAM
    model.loadFavorites()
    val favoriteTeams = valueOf(model.favorites)
    verify(observer).onChanged(favoriteTeams)

    favoriteTeams shouldExactlyEqualsWith sourceTeams
  }

  @Test
  fun loadingTest() {
    coroutineRule.pauseDispatcher()

    val observer: Observer<Boolean> = mock()
    model.loading.observeForever(observer)

    model.loadFavorites()

    val loadingBefore = valueOf(model.loading)
    verify(observer).onChanged(loadingBefore)
    assertTrue(loadingBefore)

    coroutineRule.resumeDispatcher()

    val loadingAfter = valueOf(model.loading)
    verify(observer).onChanged(loadingAfter)
    assertFalse(loadingAfter)
  }

  @Test
  fun errorMessageTest() {
    eventRepo.shouldReturnError = true

    val observer: Observer<Int> = mock()
    model.error.observeForever(observer)

    model.loadFavorites()
    val error = valueOf(model.error)
    verify(observer).onChanged(R.string.msg_user_unknown_error)

    assertEquals(R.string.msg_user_unknown_error, error)
  }

  private infix fun <T> List<T>.shouldExactlyEqualsWith(other: List<T>) {
    assertEquals(other.size, this.size)
    assertEquals(other, this)
  }
}