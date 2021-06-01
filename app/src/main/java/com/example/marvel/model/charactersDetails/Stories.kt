package com.example.marvel.model.charactersDetails

data class Stories(val collectionURI: String = "",
                   val available: Int = 0,
                   val returned: Int = 0,
                   val items: List<ItemsItem>?)