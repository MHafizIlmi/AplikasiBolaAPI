/*
 * hafiz on 11/23/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

object EspressoIdlingResource {

  @JvmField
  val idlingResource = SimpleIdlingResource("GLOBAL")

  fun increment() = idlingResource.increment()

  fun decrement() {
    if (!idlingResource.isIdleNow)
      idlingResource.decrement()
  }
}

class SimpleIdlingResource(private val resourceName: String) : IdlingResource {

  private val counter = AtomicInteger(0)

  @Volatile
  private var callback: IdlingResource.ResourceCallback? = null

  override fun getName() = resourceName

  override fun isIdleNow() = counter.get() == 0

  override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
    this.callback = callback
  }

  fun increment() = counter.getAndIncrement()

  fun decrement() {
    val counterValue = counter.decrementAndGet()
    if (counterValue == 0) callback?.onTransitionToIdle() // notify espresso we are idle now
    else check(counterValue >= 0) { "Counter corrupted!" }
  }
}

inline fun <T> wrapWithIdlingResource(block: () -> T): T {
  EspressoIdlingResource.increment()
  return try {
    block()
  } finally {
    EspressoIdlingResource.decrement()
  }
}