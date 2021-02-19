/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import id.hafiz.bolaapi.KadeApplication
import id.hafiz.bolaapi.ViewModelFactory

inline fun <reified T : ViewModel> Fragment.viewModel() = lazy {
  val appContext = requireActivity().application.applicationContext
  val repositories = (appContext as KadeApplication).repositories
  ViewModelProviders.of(requireActivity(), ViewModelFactory(repositories))[T::class.java]
}

fun <T> Fragment.observe(liveData: LiveData<T>, observe: (T) -> Unit) {
  liveData.observe(viewLifecycleOwner, Observer {
    if (it != null) observe(it)
  })
}