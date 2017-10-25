package com.zjj.daggerdatabinding.module

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zjj.daggerdatabinding.BuildConfig
import com.zjj.daggerdatabinding.api.Api
import com.zjj.daggerdatabinding.api.Constants
import com.zjj.daggerdatabinding.api.SecondApi
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import com.zjj.daggerdatabinding.utils.SystemUtils
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by zjj on 17/10/17.
 */
@Module(includes = arrayOf(AppModule::class))
class ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: HttpUrl, client: OkHttpClient, gson: Gson) =
            Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

    @Provides fun provideBaseUrl() = HttpUrl.parse(Constants.BASE_URL)
    @Provides fun provideOkhttp(context: Context, interceptor: HttpLoggingInterceptor): OkHttpClient {
        val  builder = OkHttpClient.Builder()
        if(BuildConfig.DEBUG){
            builder.addInterceptor(interceptor)
        }
        val cacheSize = 1024 * 1024 * 10L
        val cacheDir = File(context.cacheDir, "http")
        val cache = Cache(cacheDir, cacheSize)

        val cacheInterceptor = Interceptor{ chain ->
            var request = chain.request()
            if(!SystemUtils.isNetworkConnected){
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            var tryCount = 0
            var response = chain.proceed(request)
            while (!response.isSuccessful && tryCount < 3){
                tryCount++
                response = chain.proceed(request)
            }
            if(SystemUtils.isNetworkConnected){
                val maxAge = 0
                // 有网络时, 不缓存, 最大保存时长为0
                response.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).removeHeader("Pragma").build()
            }else{
                // 无网络时，设置超时为4周
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).removeHeader("Pragma").build()
            }

        }
        builder.addNetworkInterceptor(cacheInterceptor)
        builder.addInterceptor(cacheInterceptor)
        builder.cache(cache)
//        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
//        //错误重连
        builder.retryOnConnectionFailure(true)
        return builder.build()
    }
    @Provides fun provideInterceptor() :HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor{
            msg -> Log.d("okhttp",msg)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides fun provideGson() = GsonBuilder().create()
    /**
     * 第一套请求接口
     */
    @Singleton
    @Provides fun provideApi(retrofit: Retrofit) = retrofit.create(Api::class.java)

    /**
     * 有第二套请求接口，所有需要重写
     */
    @Singleton
    @Provides fun provideSecondApi(client: OkHttpClient) =  Retrofit.Builder()
            .client(client)
            .baseUrl(HttpUrl.parse(Constants.HOST))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build().create(SecondApi::class.java)

}