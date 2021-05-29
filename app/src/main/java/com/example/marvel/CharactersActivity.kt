package com.example.marvel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvel.utils.extensions.setNoLimitsWindow

class CharactersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters)
        setNoLimitsWindow()
    }
}