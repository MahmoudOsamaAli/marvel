package com.example.marvel.data

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marvel.data.MarvelRepo.Companion.NETWORK_PAGE_SIZE
import com.example.marvel.model.ResultsItem
import com.example.marvel.network.MarvelService
import com.example.marvel.utils.NetworkUtils.publicKey
import com.example.marvel.utils.NetworkUtils.timeStamp
import java.io.IOException

private const val MARVEL_OFFSET_VALUE = 0

class MarvelPagingSource(
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
        val position = params.key ?: MARVEL_OFFSET_VALUE
        Log.i(TAG, "load: current position = $position")
        return try {
            val response = service.getCharacters(publicKey,NETWORK_PAGE_SIZE,hash,timeStamp,position)
            val nextKey = if (response.data.count == 0) 0 else position + response.data.count
            LoadResult.Page(
                    data = response.data.results!!,
                    prevKey = if (position == MARVEL_OFFSET_VALUE) null else position - 1,
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