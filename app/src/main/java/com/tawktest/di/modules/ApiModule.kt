package com.tawktest.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tawktest.BuildConfig
import com.tawktest.app.Tawktest
import com.tawktest.data.remote.api.Apis
import com.tawktest.data.remote.interceptor.NetworkInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

@Module
class ApiModule {

    /**
     * Provides singleton cache to application directory
     * @param context
     * @return Cache
     * */
    @Provides
    @Singleton
    fun provideHttpCache(context: Tawktest): Cache {
        val size = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(context.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, size)
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(context: Tawktest): NetworkInterceptor {
        return NetworkInterceptor(context)
    }

    /**
     * Provides dispatcher which will manage to send only one network request at a time
     * */
    @Provides
    @Singleton
    fun provideDispatcher(): Dispatcher {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        return dispatcher
    }

    /**
     * Provides singleton OkHttpClient, Intercepts all api call and handle error with ErrorInterceptor
     * @param cache
     * @return OkHttpClient
     * */
    @Provides
    @Singleton
    fun provideOkHttpClient(
            cache: Cache,
            networkInterceptor: NetworkInterceptor,
            dispatcher: Dispatcher
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.dispatcher(dispatcher)
        builder.cache(cache)
        builder.addInterceptor(networkInterceptor)
        if (BuildConfig.DEBUG) builder.addInterceptor(logging)
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        return builder.build()
    }

    /**
     * Provides singleton Gson
     * @return Gson
     * */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return builder.create()
    }

    /**
     * Provides singleton Retrofit
     * @param gson
     * @param okHttpClient
     * @return Retrofit
     * */
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    /**
     * Provides apis for the project
     * @param retrofit
     * @return apis of the project
     * */
    @Provides
    @Singleton
    fun provideApis(retrofit: Retrofit): Apis = retrofit.create(Apis::class.java)

}