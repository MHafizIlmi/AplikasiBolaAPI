/*
 * hafiz on 12/8/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui.eventdetail

import android.content.Context
import id.hafiz.bolaapi.R
import id.hafiz.bolaapi.data.entity.Event
import id.hafiz.bolaapi.view.*
import id.hafiz.bolaapi.view.base.EntityView
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalMargin

class EventDetailView(context: Context) : _LinearLayout(context), EntityView<Event> {

  val eventBadge: EventBadgeView
  private val goal: CoupleTextView
  private val yellowCards: CoupleTextView
  private val redCards: CoupleTextView
  private val homeTeamLineup: TeamLineupView
  private val awayTeamLineup: TeamLineupView

  init {
    lparams(matchParent) { verticalMargin = dip(8) }
    orientation = VERTICAL
    eventBadge = eventBadgeView()
    goal = coupleTextView()
    yellowCards = coupleTextView()
    redCards = coupleTextView()
    homeTeamLineup = teamLineupView()
    awayTeamLineup = teamLineupView()
  }

  override fun bind(e: Event) {
    eventBadge.bind(e)

    goal.setLeftText(e.homeGoalDetails)
    goal.setRightText(e.awayGoalDetails)
    goal.setIcon(R.drawable.ic_goal, R.color.colorBall)

    yellowCards.setLeftText(e.homeYellowCards)
    yellowCards.setRightText(e.awayYellowCards)
    yellowCards.setIcon(R.drawable.ic_card, R.color.colorYellowCard)

    redCards.setLeftText(e.awayRedCards)
    redCards.setRightText(e.awayRedCards)
    redCards.setIcon(R.drawable.ic_card, R.color.colorRedCard)

    homeTeamLineup.setTitle("${e.homeName} team lineup")
    homeTeamLineup.setColor(R.color.colorHomeStadium)
    homeTeamLineup.addLine(e.homeLineupGoalkeeper)
    homeTeamLineup.addLine(e.homeLineupDefense)
    homeTeamLineup.addLine(e.homeLineupMidfield)
    homeTeamLineup.addLine(e.homeLineupForward)

    awayTeamLineup.setTitle("${e.awayName} team lineup")
    awayTeamLineup.setColor(R.color.colorAwayStadium)
    awayTeamLineup.addLine(e.awayLineupGoalkeeper)
    awayTeamLineup.addLine(e.awayLineupDefense)
    awayTeamLineup.addLine(e.awayLineupMidfield)
    awayTeamLineup.addLine(e.awayLineupForward)
  }
}