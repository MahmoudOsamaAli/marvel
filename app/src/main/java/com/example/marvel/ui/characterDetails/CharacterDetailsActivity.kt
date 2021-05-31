package com.example.marvel.ui.characterDetails

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.marvel.R
import com.example.marvel.databinding.ActivityCharacterDetailsBinding
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.squareup.picasso.Picasso

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_details)
        setNoLimitsWindow()
        init()
    }

    private fun init(){
        getIntentData()
    }

    private fun getIntentData() {
        val url: String? = intent.getStringExtra("uri")
        val name: String? = intent.getStringExtra("name")
        Log.i(TAG, "onCreate: url = $url")
        if (!url.isNullOrEmpty()) {
            Picasso.get()
                .load(Uri.parse(url))
                .into(binding.image)

            binding.characterName.text = name
        }
    }

    companion object{
        private const val TAG = "CharacterDetailsActivit"
    }
}