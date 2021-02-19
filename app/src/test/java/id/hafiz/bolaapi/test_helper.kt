package id.hafiz.bolaapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import id.hafiz.bolaapi.data.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

  override fun starting(description: Description?) {
    super.starting(description)
    Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
  }

  override fun finished(description: Description?) {
    super.finished(description)
    Dispatchers.resetMain()
  }
}

fun <T> valueOf(liveData: LiveData<T>): T {
  val data = arrayOfNulls<Any>(1)
  val latch = CountDownLatch(1)
  val observer = object : Observer<T> {
    override fun onChanged(t: T) {
      data[0] = t
      latch.countDown()
      liveData.removeObserver(this)
    }
  }
  liveData.observeForever(observer)
  latch.await(2, TimeUnit.SECONDS)
  @Suppress("UNCHECKED_CAST") return data[0] as T
}

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

/**
 * Return state success value if possible
 */
val <T> State<T>.succeed
  get() = throwOrElse("State has no success value!") { (this as State.Success).data }

/**
 * Return state error message if possible
 */
val <T> State<T>.error
  get() = throwOrElse("State is not error!") { (this as State.Error).message }

// -- Private members -- //

private fun <T> throwOrElse(
  message: String, action: () -> T
) = try {
  action()
} catch (e: ClassCastException) {
  throw IllegalStateException(message)
}