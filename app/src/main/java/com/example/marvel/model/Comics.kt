package com.example.marvel.model

data class Comics(val collectionURI: String = "",
                  val available: Int = 0,
                  val returned: Int = 0,
                  val items: List<ItemsItem>?)