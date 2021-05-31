package com.example.marvel.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.marvel.data.MarvelRepo
import com.example.marvel.model.ResultsItem
import com.example.marvel.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class MainViewModel(private val repository: MarvelRepo) : ViewModel() {

    private var currentSearchResult: Flow<PagingData<ResultsItem>>? = null

    fun loadDataFromNetwork(): Flow<PagingData<ResultsItem>> {
        val newResult: Flow<PagingData<ResultsItem>> = repository.getSearchResultStream(NetworkUtils.getHash())
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
