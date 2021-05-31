package com.example.marvel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.marvel.databinding.SplashMainBinding
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: SplashMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_main)
        setNoLimitsWindow()
        init()
    }

    private fun init(){

        Glide.with(this)
            .load(R.drawable.mcu_background)
            .apply(bitmapTransform(BlurTransformation(22,5)))
            .into(binding.imageBackground)

        MainScope().launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity,CharactersActivity::class.java))
            finish()
        }

    }
}