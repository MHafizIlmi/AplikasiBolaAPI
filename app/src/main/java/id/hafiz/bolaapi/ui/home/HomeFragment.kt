/*
 * hafiz on 12/2/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.google.android.material.tabs.TabLayout
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.view.searchMenuEnable
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager

class HomeFragment : Fragment() {

  private lateinit var homePager: ViewPager
  private lateinit var homeTab: TabLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    state: Bundle?
  ): View? = UI {
    coordinatorLayout {
      id = R.id.homeFragment
      lparams(matchParent, matchParent)
      // viewPager
      homePager = viewPager {
        id = R.id.homePager
      }.lparams(matchParent, matchParent) {
        behavior = AppBarLayout.ScrollingViewBehavior()
      }
      // appBarLayout
      appBarLayout {
        id = R.id.homeToolbarContainer
        lparams(matchParent)
        // toolbar
        toolbar {
          id = R.id.homeToolbar
          setTitle(R.string.app_name)
          searchMenuEnable()
        }.lparams(matchParent) {
          scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
        }
        // tabLayout
        homeTab = tabLayout {
          id = R.id.homeTabLayout
          lparams(matchParent)
        }
      }
    }
  }.view

  override fun onViewCreated(view: View, state: Bundle?) {
    super.onViewCreated(view, state)
    homeTab.setupWithViewPager(homePager)
    homePager.adapter = HomeAdapter(requireContext(), childFragmentManager)
  }
}