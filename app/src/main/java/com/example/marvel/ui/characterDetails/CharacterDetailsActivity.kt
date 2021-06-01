package com.example.marvel.ui.characterDetails

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.marvel.R
import com.example.marvel.databinding.ActivityCharacterDetailsBinding
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.example.marvel.utils.Injection
import com.example.marvel.viewModels.CharacterDetailsViewModel
import com.example.marvel.viewModels.MainViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailsBinding
    private val viewModel: CharacterDetailsViewModel by lazy{
        ViewModelProvider(this, Injection.provideViewModelFactory())[CharacterDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_details)
        setNoLimitsWindow()
        init()
    }

    private fun init() {
        getIntentData()
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    private fun getIntentData() {
        val url: String? = intent.getStringExtra("uri")
        val name: String? = intent.getStringExtra("name")
        Log.i(TAG, "onCreate: url = $url")
        if (!url.isNullOrEmpty()) {

            Glide.with(this)
                .load(Uri.parse(url))
                .into(binding.image)

            Glide.with(this)
                .load(Uri.parse(url))
                .apply(RequestOptions.bitmapTransform(BlurTransformation(22, 5)))
                .into(binding.backgoundImage)

            binding.characterName.text = name
        }
    }

    companion object {
        private const val TAG = "CharacterDetailsActivit"
    }
}