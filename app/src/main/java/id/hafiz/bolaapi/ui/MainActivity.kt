package id.hafiz.bolaapi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.hafiz.bolaapi.R

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    val hostFragment = NavHostFragment.create(R.navigation.main_nav_graph)
//    This implement make state lost on config changes
//    frameLayout {
//      id = R.id.mainContainer
//      lparams(matchParent, matchParent)
//      supportFragmentManager.beginTransaction().apply {
//        replace(R.id.mainContainer, hostFragment)
//        setPrimaryNavigationFragment(hostFragment)
//        commit()
//      }
//    }
  }
}