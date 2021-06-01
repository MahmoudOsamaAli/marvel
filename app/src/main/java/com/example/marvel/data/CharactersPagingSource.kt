package com.example.marvel.data

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marvel.viewModels.MarvelRepo.Companion.NETWORK_PAGE_SIZE
import com.example.marvel.model.characters.ResultsItem
import com.example.marvel.network.MarvelService
import com.example.marvel.utils.NetworkUtils.publicKey
import com.example.marvel.utils.NetworkUtils.timeStamp
import java.io.IOException

private const val CHARACTERS_OFFSET_VALUE = 0

class CharactersPagingSource(
        private val service: MarvelService,
        private val hash: String
) : PagingSource<Int, ResultsItem>() {

    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        val offset = params.key ?: CHARACTERS_OFFSET_VALUE
        Log.i(TAG, "load: current position = $offset")
        return try {
            val response = service.getCharacters(publicKey,NETWORK_PAGE_SIZE,hash,timeStamp,offset)
            val nextKey = if (response.data.count == 0 ||response.data.count < NETWORK_PAGE_SIZE ) null else offset + response.data.count
            LoadResult.Page(
                    data = response.data.results!!,
                    prevKey = if (offset == CHARACTERS_OFFSET_VALUE) null else offset - 1,
                    nextKey = nextKey
            )
        }catch (e:IOException){
            return LoadResult.Error(e)
        }catch (ex:NetworkErrorException){
            return LoadResult.Error(ex)
        }
    }

    companion object{
        private const val TAG = "MarvelPagingSource"
    }

}