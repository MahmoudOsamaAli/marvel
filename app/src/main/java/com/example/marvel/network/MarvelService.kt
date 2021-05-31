package com.example.marvel.network

import com.example.marvel.model.MarvelResponse
import com.example.marvel.utils.NetworkUtils.baseUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {
    /**
     * Get Characters.
     */
    @GET("characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Int,
        @Query("offset") offset: Int,
    ): MarvelResponse

    companion object {

        fun create(): MarvelService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarvelService::class.java)
        }
    }
}
