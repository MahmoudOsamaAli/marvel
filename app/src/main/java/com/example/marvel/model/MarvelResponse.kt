package com.example.marvel.model

data class MarvelResponse(val copyright: String,
                          val code: Int,
                          val data: Data,
                          val attributionHTML: String,
                          val attributionText: String,
                          val etag: String,
                          val status: String)