package com.example.marvel.callBacks

import android.view.View
import com.example.marvel.model.characters.ResultsItem

interface OnItemClick {
    fun onItemClick(item:Any)
}

interface OnCharacterClicked{
    fun onItemClicked(item: ResultsItem, view: View)
}