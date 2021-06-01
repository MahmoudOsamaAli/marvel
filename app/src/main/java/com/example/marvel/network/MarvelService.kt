package com.example.marvel.network

import com.example.marvel.model.characters.CharactersResponse
import com.example.marvel.model.charactersDetails.CharacterDetailsResponse
import com.example.marvel.utils.NetworkUtils.baseUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
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
    ): CharactersResponse

    @GET("characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Int,
    ): CharactersResponse?

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Int,
        @Query("offset") offset: Int,

    ): CharacterDetailsResponse

    @GET("characters/{characterId}/events")
    suspend fun getEvents(
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Int,
        @Query("offset") offset: Int,
    ): CharacterDetailsResponse

    @GET("characters/{characterId}/series")
    suspend fun getSeries(
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Int,
        @Query("offset") offset: Int,
    ): CharacterDetailsResponse

    @GET("characters/{characterId}/stories")
    suspend fun getStories(
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Int,
        @Query("offset") offset: Int,
    ): CharacterDetailsResponse

    companion object {

        fun create(): MarvelService {
            val service:MarvelService? = null
            return if (service == null){
                val logger = HttpLoggingInterceptor()
                logger.level = Level.BASIC

                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
                Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MarvelService::class.java)
            }else service

        }
    }
}
