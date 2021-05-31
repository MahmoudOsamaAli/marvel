package com.example.marvel.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel.model.ResultsItem
import com.example.marvel.network.MarvelService
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that works with local and remote data sources.
 */
class MarvelRepo(private val service: MarvelService) {

    fun getSearchResultStream(hash: String): Flow<PagingData<ResultsItem>> {
        return Pager(
                config = PagingConfig(
                        pageSize = NETWORK_PAGE_SIZE,
                        enablePlaceholders = true
                ),
                pagingSourceFactory = { MarvelPagingSource(service, hash) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}
