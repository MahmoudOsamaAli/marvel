package com.example.marvel.model.charactersDetails

data class Data(val total: Int = 0,
                val offset: Int = 0,
                val limit: Int = 0,
                val count: Int = 0,
                val results: List<ResultsItem>?)