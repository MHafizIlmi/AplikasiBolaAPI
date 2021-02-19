package id.hafiz.bolaapi.data.source.embedded

import id.hafiz.bolaapi.data.entity.League

class LeagueDataSource {

  fun getLeagues() = listOf(
    League(
      id = 4328,
      name = "English Premier League",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/i6o0kh1549879062.png"
    ),
    League(
      id = 4334,
      name = "French Ligue 1",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/8f5jmf1516458074.png"
    ),
    League(
      id = 4331,
      name = "German Bundesliga",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/0j55yv1534764799.png"
    ),
    League(
      id = 4332,
      name = "Italian Serie A",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/ocy2fe1566216901.png"
    ),
    League(
      id = 4335,
      name = "Spanish La Liga",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/7onmyv1534768460.png"
    ),
    League(
      id = 4346,
      name = "American Mayor League",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/dqo6r91549878326.png"
    ),
    League(
      id = 4344,
      name = "Portuguese Primeira Liga",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/cplp641535733210.png"
    ),
    League(
      id = 4356,
      name = "Australian A League",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/sfhanl1547383730.png"
    ),
    League(
      id = 4330,
      name = "Scotish Premier League",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/vw72bl1534096708.png"
    ),
    League(
      id = 4396,
      name = "English League 1",
      badgeUrl = "https://www.thesportsdb.com/images/media/league/badge/vm1qlp1535986713.png"
    )
  )
}