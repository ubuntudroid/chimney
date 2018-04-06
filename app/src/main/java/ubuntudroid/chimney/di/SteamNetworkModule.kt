package ubuntudroid.chimney.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ubuntudroid.chimney.BuildConfig

const val STEAM_SCOPE = "steam"

val steamNetworkModule = applicationContext {
    // OkHttpClient
    factory(STEAM_SCOPE) {
        OkHttpClient.Builder()
                .addInterceptor {
                    val request = it.request()
                    val url = request.url()
                    val authenticatedUrl = url.newBuilder()
                            .addQueryParameter("key", BuildConfig.STEAM_API_TOKEN)
                            .build()
                    it.proceed(
                            request.newBuilder()
                                    .url(authenticatedUrl)
                                    .build()
                    )
                }
                .build()
    }

    // Retrofit
    bean(STEAM_SCOPE) {
        Retrofit.Builder()
                .baseUrl(BuildConfig.STEAM_BASE_URL)
                .client(get(STEAM_SCOPE))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}