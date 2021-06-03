package com.example.marvel.viewModels

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel.data.CharactersDetailsPagingSource
import com.example.marvel.data.CharactersPagingSource
import com.example.marvel.model.characters.CharactersResponse
import com.example.marvel.network.MarvelService
import com.example.marvel.utils.NetworkUtils.EndPointType
import com.example.marvel.utils.NetworkUtils.publicKey
import com.example.marvel.utils.NetworkUtils.timeStamp
import kotlinx.coroutines.flow.Flow
import java.io.IOException

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

    suspend fun getCharacterById(hash: String, id: Int): CharactersResponse? {
        return try {
            service.getCharacterById(id.toString(), publicKey, hash, timeStamp)
        } catch (ex: IOException) {
            null
        } catch (e: NetworkErrorException) {
            null
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
        private const val TAG = "MarvelRepo"
    }
}
