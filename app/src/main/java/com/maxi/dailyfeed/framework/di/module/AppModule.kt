package com.maxi.dailyfeed.framework.di.module

import android.content.Context
import android.util.Base64
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.maxi.dailyfeed.BuildConfig
import com.maxi.dailyfeed.common.DefaultDispatcherProvider
import com.maxi.dailyfeed.common.DefaultNetworkConnectivityHelper
import com.maxi.dailyfeed.common.DispatcherProvider
import com.maxi.dailyfeed.common.NetworkConnectivityHelper
import com.maxi.dailyfeed.data.source.local.NewsDatabase
import com.maxi.dailyfeed.data.source.local.dao.NewsDao
import com.maxi.dailyfeed.data.source.remote.api.NetworkApiService
import com.maxi.dailyfeed.data.source.remote.interceptor.AuthorizationInterceptor
import com.maxi.dailyfeed.data.source.remote.interceptor.CacheControlInterceptor
import com.maxi.dailyfeed.data.source.remote.interceptor.ErrorHandlingInterceptor
import com.maxi.dailyfeed.data.source.remote.interceptor.LoggingInterceptor
import com.maxi.dailyfeed.framework.di.qualifier.ApiKey
import com.maxi.dailyfeed.framework.di.qualifier.BaseUrl
import com.maxi.dailyfeed.framework.di.qualifier.IsDebug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val NEWS_CACHE = "news_cache"
    private const val NEWS_DATABASE = "news"

    @Provides
    @BaseUrl
    fun provideBaseUrl():
            String =
        "https://newsapi.org/v2/"

    @Provides
    @ApiKey
    fun provideApiKey():
            String =
        String(
            Base64.decode(
                BuildConfig.API_KEY,
                Base64.DEFAULT
            ),
            Charsets.UTF_8
        )

    @Provides
    @IsDebug
    fun provideIsDebug():
            Boolean =
        BuildConfig.DEBUG

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        }

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(
        @ApiKey apiKey: String
    ): AuthorizationInterceptor =
        AuthorizationInterceptor(
            apiKey,
            BuildConfig.USER_AGENT
        )

    @Provides
    @Singleton
    fun provideErrorHandlingInterceptor(
        json: Json
    ): ErrorHandlingInterceptor =
        ErrorHandlingInterceptor(json)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(
        @IsDebug isDebug: Boolean
    ): HttpLoggingInterceptor =
        LoggingInterceptor(isDebug).create()

    @Provides
    @Singleton
    fun provideCacheControlInterceptor():
            CacheControlInterceptor =
        CacheControlInterceptor()

    @Provides
    @Singleton
    fun provideCache(
        @ApplicationContext context: Context
    ): Cache =
        Cache(
            File(
                context.cacheDir,
                NEWS_CACHE
            ),
            10L * 1024 * 1024
        )

    @Provides
    @Singleton
    fun httpClient(
        cache: Cache,
        authorizationInterceptor: AuthorizationInterceptor,
        cacheControlInterceptor: CacheControlInterceptor,
        errorHandlingInterceptor: ErrorHandlingInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .cache(cache)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(cacheControlInterceptor)
            .addInterceptor(errorHandlingInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        httpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkApiService(
        retrofit: Retrofit
    ): NetworkApiService =
        retrofit.create(NetworkApiService::class.java)

    @Provides
    @Singleton
    fun provideNewsDatabase(
        @ApplicationContext context: Context
    ): NewsDatabase =
        Room
            .databaseBuilder(
                context,
                NewsDatabase::class.java,
                NEWS_DATABASE
            ).build()

    @Provides
    @Singleton
    fun provideNewsDao(
        database: NewsDatabase
    ): NewsDao =
        database.newsDao()

    @Provides
    @Singleton
    fun provideDispatcherProvider():
            DispatcherProvider =
        DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideNetworkConnectivityHelper(
        @ApplicationContext context: Context
    ): NetworkConnectivityHelper =
        DefaultNetworkConnectivityHelper(context)
}