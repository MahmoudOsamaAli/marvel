package com.example.marvel.utils

import androidx.lifecycle.ViewModelProvider
import com.example.marvel.ViewModelFactory
import com.example.marvel.data.MarvelRepo
import com.example.marvel.network.MarvelService

object Injection {

    private fun provideMarvelRepository(): MarvelRepo {
        return MarvelRepo(MarvelService.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideMarvelRepository())
    }
}
