package com.example.marvel.viewModels

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel.data.CharactersDetailsPagingSource
import com.example.marvel.data.CharactersPagingSource
import com.example.marvel.model.characters.CharactersResponse
import com.example.marvel.network.MarvelService
import com.example.marvel.utils.NetworkUtils
import com.example.marvel.utils.NetworkUtils.publicKey
import com.example.marvel.utils.NetworkUtils.timeStamp
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that works with local and remote data sources.
 */
class MarvelRepo(private val service: MarvelService) {

    fun getCharactersStream(hash: String): Flow<PagingData<com.example.marvel.model.characters.ResultsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CharactersPagingSource(service, hash) }
        ).flow
    }

    fun getCharacterComicsStream(
        hash: String,
        characterId: Int
    ): Flow<PagingData<com.example.marvel.model.charactersDetails.ResultsItem>> {
        Log.i(TAG, "getCharacterComicsStream: ")
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CharactersDetailsPagingSource(
                    service,
                    hash,
                    characterId,
                    EndPointType.COMICS
                )
            }
        ).flow
    }

    fun getCharacterEventsStream(
        hash: String,
        characterId: Int
    ): Flow<PagingData<com.example.marvel.model.charactersDetails.ResultsItem>> {
        Log.i(TAG, "getCharacterEventsStream: ")
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CharactersDetailsPagingSource(
                    service,
                    hash,
                    characterId,
                    EndPointType.EVENTS
                )
            }
        ).flow
    }

    fun getCharacterSeriesStream(
        hash: String,
        characterId: Int
    ): Flow<PagingData<com.example.marvel.model.charactersDetails.ResultsItem>> {
        Log.i(TAG, "getCharacterSeriesStream: ")
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CharactersDetailsPagingSource(
                    service,
                    hash,
                    characterId,
                    EndPointType.SERIES
                )
            }
        ).flow
    }

    fun getCharacterStoriesStream(
        hash: String,
        characterId: Int
    ): Flow<PagingData<com.example.marvel.model.charactersDetails.ResultsItem>> {
        Log.i(TAG, "getCharacterStoriesStream: ")
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CharactersDetailsPagingSource(
                    service,
                    hash,
                    characterId,
                    EndPointType.STORIES
                )
            }
        ).flow
    }

    suspend fun getCharacterById(hash:String,id:Int):CharactersResponse{
        return service.getCharacterById(id.toString(),publicKey,hash, timeStamp)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
        private const val TAG = "MarvelRepo"
    }
}

enum class EndPointType {
    COMICS, EVENTS, SERIES, STORIES
}
