package com.example.marvel.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.marvel.model.charactersDetails.ResultsItem
import com.example.marvel.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(private val repository: MarvelRepo) : ViewModel() {

    private val character:MutableLiveData<com.example.marvel.model.characters.ResultsItem> by lazy { MutableLiveData() }
    val characterLiveData:LiveData<com.example.marvel.model.characters.ResultsItem> = character
    private val selectedItemThumbnail:MutableLiveData<String> by lazy { MutableLiveData() }
    val selectedItemThumbnailLiveData:LiveData<String> = selectedItemThumbnail

    private var characterId: Int = 0

    fun setCharacterId(id: Int) {
        Log.i(TAG, "getCharacterById: character id = $id")
        characterId = id
    }

    fun getCharacterById(){
        viewModelScope.launch {
            val response = repository.getCharacterById(NetworkUtils.getHash(), characterId)
            if (response != null)character.postValue(response.data.results?.get(0))
        }
    }

    fun getCharacterComicsFromNetwork(): Flow<PagingData<ResultsItem>> {
        return repository.getCharacterComicsStream(NetworkUtils.getHash(), characterId)
            .cachedIn(viewModelScope)
    }

    fun getCharacterEventsFromNetwork(): Flow<PagingData<ResultsItem>> {
        return repository.getCharacterEventsStream(NetworkUtils.getHash(), characterId)
            .cachedIn(viewModelScope)
    }

    fun getCharacterSeriesFromNetwork(): Flow<PagingData<ResultsItem>> {
        return repository.getCharacterSeriesStream(NetworkUtils.getHash(), characterId)
            .cachedIn(viewModelScope)
    }

    fun setSelectedItemThumbnail(url:String){
        selectedItemThumbnail.value = url
    }

    companion object{
        private const val TAG = "CharacterDetailsViewMod"
    }

}
