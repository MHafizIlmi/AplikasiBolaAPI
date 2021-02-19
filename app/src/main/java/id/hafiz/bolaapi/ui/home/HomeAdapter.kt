/*
 * hafiz on 12/2/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.home

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.ui.favorite.FavoriteFragment
import id.hafiz.bolaapi.ui.league.LeagueFragment

class HomeAdapter(context: Context, manager: FragmentManager) :
  FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  private val fragments = linkedMapOf(
    context.getString(R.string.title_league) to LeagueFragment(),
    context.getString(R.string.title_favorite) to FavoriteFragment()
  )

  override fun getCount() = fragments.size

  override fun getItem(position: Int) =
    fragments.values.toList()[position]

  override fun getPageTitle(position: Int) =
    fragments.keys.toList()[position]
}