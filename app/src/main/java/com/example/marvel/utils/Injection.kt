package com.example.marvel.utils

import androidx.lifecycle.ViewModelProvider
import com.example.marvel.viewModels.ViewModelFactory
import com.example.marvel.data.MarvelRepo
import com.example.marvel.network.MarvelService

object Injection {

    private fun provideMarvelRepository(): MarvelRepo = MarvelRepo(MarvelService.create())

    fun provideViewModelFactory(): ViewModelProvider.Factory = ViewModelFactory(provideMarvelRepository())

}
