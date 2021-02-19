/*
 * hafiz on 11/1/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi

import android.content.Context
import com.google.gson.GsonBuilder
import id.hafiz.bolaapi.data.source.DefaultEventRepository
import id.hafiz.bolaapi.data.source.DefaultLeagueRepository
import id.hafiz.bolaapi.data.source.DefaultTeamRepository
import id.hafiz.bolaapi.data.source.embedded.LeagueDataSource
import id.hafiz.bolaapi.data.source.local.EventDataSource
import id.hafiz.bolaapi.data.source.local.SportDBHelper
import id.hafiz.bolaapi.data.source.local.TeamDataSource
import id.hafiz.bolaapi.data.source.remote.TheSportDbService
import id.hafiz.bolaapi.data.source.repository.EventRepository
import id.hafiz.bolaapi.data.source.repository.LeagueRepository
import id.hafiz.bolaapi.data.source.repository.TeamRepository
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {

  @Volatile
  private var leagueRepository: LeagueRepository? = null

  @Volatile
  private var eventRepository: EventRepository? = null

  @Volatile
  private var teamRepository: TeamRepository? = null

  fun provideLeagueRepository(context: Context) = leagueRepository ?: synchronized(this) {
    leagueRepository ?: DefaultLeagueRepository(
      embeddedSource = LeagueDataSource(),
      cacheableRemoteSource = createTheSportDbService(context, true),
      remoteSource = createTheSportDbService(context)
    )
  }

  fun provideEventRepository(context: Context) = eventRepository ?: synchronized(this) {
    eventRepository ?: DefaultEventRepository(
      remoteSource = createTheSportDbService(context),
      cacheableRemoteSource = createTheSportDbService(context, true),
      localSource = createEventDataSource(context)
    )
  }

  fun provideTeamRepository(context: Context) = teamRepository ?: synchronized(this) {
    teamRepository ?: DefaultTeamRepository(
      cacheableRemoteSource = createTheSportDbService(context, true),
      localSource = createTeamDataSource(context)
    )
  }

  private fun createTheSportDbService(
    context: Context,
    cacheable: Boolean = false
  ): TheSportDbService {
    val baseUrl = "https://www.thesportsdb.com/api/v1/json/1/"
    val okHttpClient = OkHttpClient.Builder()
      .connectTimeout(1, TimeUnit.MINUTES)
      .readTimeout(1, TimeUnit.MINUTES)

    // enable caching for data that changes over
    // a long period of time like League or Team
    if (cacheable) {
      val cacheSize = (10 * 1024 * 1024).toLong()
      val cache = Cache(context.cacheDir, cacheSize)

      val cacheControl = CacheControl.Builder()
        .maxStale(7, TimeUnit.DAYS)
        .build()

      okHttpClient
        .cache(cache)
        .addInterceptor { chain ->
          val request = chain
            .request()
            .newBuilder()
            .cacheControl(cacheControl)
            .build()
          chain.proceed(request)
        }
    }

    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
      .client(okHttpClient.build())
      .build()
      .create(TheSportDbService::class.java)
  }

  private fun createEventDataSource(context: Context): EventDataSource {
    val dbHelper = sportDBHelper(context)
    return EventDataSource(dbHelper)
  }

  private fun createTeamDataSource(context: Context): TeamDataSource {
    val dbHelper = sportDBHelper(context)
    return TeamDataSource(dbHelper)
  }

  private fun sportDBHelper(context: Context): SportDBHelper {
    val dbName = context.getString(R.string.db_name)
    return SportDBHelper(context, dbName)
  }
}
