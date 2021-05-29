package com.example.marvel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.example.marvel.databinding.ActivityCharactersBinding
import com.example.marvel.utils.Extensions.setNoLimitsWindow

class CharactersActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCharactersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_characters)
        setNoLimitsWindow()
        setSupportActionBar(binding.toolbar)
    }

}