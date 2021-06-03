package com.example.marvel.viewModels

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.marvel.model.characters.ResultsItem
import com.example.marvel.ui.characterDetails.CharacterDetailsActivity
import com.example.marvel.ui.charactersActivity.CharactersActivity
import com.example.marvel.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: MarvelRepo) : ViewModel() {

    val charactersList: MutableLiveData<ArrayList<ResultsItem>> by lazy {
        MutableLiveData(
            arrayListOf()
        )
    }

    val charactersListLiveData: LiveData<ArrayList<ResultsItem>> = charactersList

    fun loadDataFromNetwork(): Flow<PagingData<ResultsItem>> {
        return repository.getCharactersStream(NetworkUtils.getHash())
            .cachedIn(viewModelScope)
    }

    fun startCharactersDetailedActivity(
        item: ResultsItem,
        view: View,
        charactersActivity: CharactersActivity
    ) {
        val intent = Intent(charactersActivity, CharacterDetailsActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            charactersActivity,
            view, ViewCompat.getTransitionName(view)!!
        )
        val url = item.thumbnail.path + "." + item.thumbnail.extension
        intent.putExtra("uri", url)
        intent.putExtra("name", item.name)
        intent.putExtra("id", item.id)
        charactersActivity.startActivity(intent, options.toBundle())
    }
}
