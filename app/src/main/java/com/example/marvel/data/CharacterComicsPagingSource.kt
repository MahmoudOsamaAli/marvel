package com.example.marvel.data

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marvel.model.charactersDetails.CharacterDetailsResponse
import com.example.marvel.viewModels.MarvelRepo.Companion.NETWORK_PAGE_SIZE
import com.example.marvel.model.charactersDetails.ResultsItem
import com.example.marvel.network.MarvelService
import com.example.marvel.utils.NetworkUtils.publicKey
import com.example.marvel.utils.NetworkUtils.timeStamp
import com.example.marvel.viewModels.EndPointType
import java.io.IOException

private const val CHARACTERS_OFFSET_VALUE = 0

class CharactersDetailsPagingSource(
    private val service: MarvelService,
    private val hash: String,
    private val characterId: Int,
    private val type: EndPointType
) : PagingSource<Int, ResultsItem>() {

    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        val offset = params.key ?: CHARACTERS_OFFSET_VALUE
        return try {
            val response: CharacterDetailsResponse
            Log.i(TAG, "load: $type")
            when (type) {
                EndPointType.COMICS -> response = service.getComics(
                    characterId.toString(),
                    publicKey,
                    NETWORK_PAGE_SIZE,
                    hash,
                    timeStamp,
                    offset
                )
                EndPointType.SERIES -> response = service.getSeries(
                    characterId.toString(),
                    publicKey,
                    NETWORK_PAGE_SIZE,
                    hash,
                    timeStamp,
                    offset
                )
                EndPointType.EVENTS -> response = service.getEvents(
                    characterId.toString(),
                    publicKey,
                    NETWORK_PAGE_SIZE,
                    hash,
                    timeStamp,
                    offset
                )
                else -> response = service.getStories(
                    characterId.toString(),
                    publicKey,
                    NETWORK_PAGE_SIZE,
                    hash,
                    timeStamp,
                    offset
                )
            }
            val nextKey =
                if (response.data.count == 0 || response.data.count < NETWORK_PAGE_SIZE) null else offset + response.data.count
            LoadResult.Page(
                data = response.data.results!!,
                prevKey = if (offset == CHARACTERS_OFFSET_VALUE) null else offset - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (ex: NetworkErrorException) {
            return LoadResult.Error(ex)
        }
    }

    companion object{
        private const val TAG = "CharacterComicsPagingSo"
    }
}