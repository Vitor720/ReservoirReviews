package com.ddapps.reservoirreviews.data.remote

import com.ddapps.reservoirreviews.data.models.MovieDataResponse
import com.ddapps.reservoirreviews.utils.BASE_URL
import com.ddapps.reservoirreviews.utils.BASE_kEY
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestApi {
    private val api: IMovieApi by lazy { provideMovieApi(provideRetrofit()) }

    private fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(50, TimeUnit.SECONDS)
            .connectTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun provideMovieApi(retrofit: Retrofit): IMovieApi{
        return retrofit.create(IMovieApi::class.java)
    }

   suspend fun getMoviesByTitle(name: String, offset: Int = 0): MovieDataResponse{
        return api.getMoviesByName(name, BASE_kEY, offset)
    }
}