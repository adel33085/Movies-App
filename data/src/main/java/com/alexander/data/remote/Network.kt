package com.alexander.data.remote

import com.alexander.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {

    lateinit var retrofit: Retrofit
    // TODO() add your movie_db api key
    const val MOVIE_DB_API_KEY = BuildConfig.MOVIE_DB_API_KEY
    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }

    fun init(baseUrl: String, isDebug: Boolean = false) {
        retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(buildClient(isDebug))
            .build()
    }

    private fun buildClient(isDebug: Boolean): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(ApiInterceptor)
        if (isDebug) {
            client.addInterceptor(logging)
        }
        return client.build()
    }

    object ApiInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val url = chain
                .request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", MOVIE_DB_API_KEY)
                .build()
            val request = chain
                .request()
                .newBuilder()
                .url(url)
                .build()
            return chain.proceed(request)
        }
    }
}
