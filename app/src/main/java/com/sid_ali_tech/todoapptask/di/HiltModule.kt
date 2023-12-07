package com.sid_ali_tech.todoapptask.di

import android.content.Context
import com.sid_ali_tech.todoapptask.common.Constants
import com.sid_ali_tech.todoapptask.data.repository.TodosRepositoryImpl
import com.google.gson.GsonBuilder
import com.sid_ali_tech.todoapptask.data.local.AppDatabase
import com.sid_ali_tech.todoapptask.data.remote.ApiService
import com.sid_ali_tech.todoapptask.datastore.PreferenceDataStoreHelper
import com.sid_ali_tech.todoapptask.domain.repository.TodosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Singleton
    @Provides
    fun providesWebApiInterface(): ApiService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val gson = GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        httpClient.addInterceptor(Interceptor { chain ->
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .build()
            val requestBuilder: Request.Builder = original.newBuilder()
                .url(url)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        })
        httpClient.readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build().create(ApiService::class.java)
    }

    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase(context)
    }

    @Provides
    fun providesDataStore(@ApplicationContext context: Context): PreferenceDataStoreHelper {
        return PreferenceDataStoreHelper(context)
    }

    @Provides
    fun providesGetTodosRepository(apiService: ApiService,appDatabase: AppDatabase,preferenceDataStoreHelper: PreferenceDataStoreHelper): TodosRepository {
        return TodosRepositoryImpl(apiService,appDatabase,preferenceDataStoreHelper)
    }

}



