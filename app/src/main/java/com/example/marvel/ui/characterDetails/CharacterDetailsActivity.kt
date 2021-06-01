package com.example.marvel.ui.characterDetails

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.marvel.R
import com.example.marvel.callBacks.OnItemClick
import com.example.marvel.databinding.ActivityCharacterDetailsBinding
import com.example.marvel.model.charactersDetails.ResultsItem
import com.example.marvel.ui.DisplayImageDialog
import com.example.marvel.ui.characterDetails.adapters.CharactersDetailsLoadStateAdapter
import com.example.marvel.ui.characterDetails.adapters.DetailsAdapter
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.example.marvel.utils.Injection
import com.example.marvel.viewModels.CharacterDetailsViewModel
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.splash_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CharacterDetailsActivity : AppCompatActivity(), OnItemClick {

    private lateinit var binding: ActivityCharacterDetailsBinding
    private val viewModel: CharacterDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideViewModelFactory()
        )[CharacterDetailsViewModel::class.java]
    }
    private val comicsAdapter by lazy { DetailsAdapter(this) }
    private val eventsAdapter by lazy { DetailsAdapter(this) }
    private val seriesAdapter by lazy { DetailsAdapter(this) }
    private val storiesAdapter by lazy { DetailsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_details)
        setNoLimitsWindow()
        init()
    }

    private fun init() {
        getIntentData()
        binding.backButton.setOnClickListener { onBackPressed() }
        initAdapters()
        getData()
        initLoading()
    }

    private fun initLoading() {
        comicsAdapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading && comicsAdapter.itemCount > 0) {
                binding.comicsAnimator.displayedChild = 1
            } else if (it.refresh is LoadState.NotLoading && comicsAdapter.itemCount == 0) {
                binding.comicsAnimator.displayedChild = 2
            } else if (it.refresh is LoadState.Error) {
                binding.comicsAnimator.displayedChild = 2
            }
        }

        eventsAdapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading && eventsAdapter.itemCount > 0) {
                binding.eventsAnimator.displayedChild = 1
            } else if (it.refresh is LoadState.NotLoading && eventsAdapter.itemCount == 0) {
                binding.eventsAnimator.displayedChild = 2
            } else if (it.refresh is LoadState.Error) {
                binding.eventsAnimator.displayedChild = 2
            }
        }

        seriesAdapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading && seriesAdapter.itemCount > 0) {
                binding.seriesAnimator.displayedChild = 1
            } else if (it.refresh is LoadState.NotLoading && seriesAdapter.itemCount == 0) {
                binding.seriesAnimator.displayedChild = 2
            } else if (it.refresh is LoadState.Error) {
                binding.seriesAnimator.displayedChild = 2
            }
        }

        storiesAdapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading && storiesAdapter.itemCount > 0) {
                binding.storiesAnimator.displayedChild = 1
            } else if (it.refresh is LoadState.NotLoading && storiesAdapter.itemCount == 0) {
                binding.storiesAnimator.displayedChild = 2
            } else if (it.refresh is LoadState.Error) {
                binding.storiesAnimator.displayedChild = 2
            }
        }
    }

    private fun getIntentData() {
        val url: String? = intent.getStringExtra("uri")
        val name: String? = intent.getStringExtra("name")
        val id: Int = intent.getIntExtra("id", 0)
        if (!url.isNullOrEmpty() && !url.contains("image_not_available")) setImages(url)
        binding.characterName.text = name
        viewModel.setCharacterId(id)
    }

    private fun setImages(url: String) {
        Log.i(TAG, "image header: $url")
        Glide.with(this)
            .load(Uri.parse(url))
            .into(binding.imageHeader)

        Glide.with(this)
            .load(Uri.parse(url))
            .apply(RequestOptions.bitmapTransform(BlurTransformation(22, 5)))
            .into(binding.backgoundImage)
    }

    private fun initAdapters() {
        binding.comicsList.adapter = comicsAdapter.withLoadStateHeaderAndFooter(
            header = CharactersDetailsLoadStateAdapter { comicsAdapter.retry() },
            footer = CharactersDetailsLoadStateAdapter { comicsAdapter.retry() }
        )
        binding.eventsList.adapter = eventsAdapter.withLoadStateHeaderAndFooter(
            header = CharactersDetailsLoadStateAdapter { eventsAdapter.retry() },
            footer = CharactersDetailsLoadStateAdapter { eventsAdapter.retry() }
        )
        binding.seriesList.adapter = seriesAdapter.withLoadStateHeaderAndFooter(
            header = CharactersDetailsLoadStateAdapter { seriesAdapter.retry() },
            footer = CharactersDetailsLoadStateAdapter { seriesAdapter.retry() }
        )
        binding.storiesList.adapter = storiesAdapter.withLoadStateHeaderAndFooter(
            header = CharactersDetailsLoadStateAdapter { storiesAdapter.retry() },
            footer = CharactersDetailsLoadStateAdapter { storiesAdapter.retry() }
        )
    }

    private fun getData() {
        lifecycleScope.launch {

            viewModel.getCharacterById()
            viewModel.characterLiveData.observe(this@CharacterDetailsActivity, { data ->
                if (data.description.isNotEmpty()) binding.description.text = data.description
            })
            viewModel.getCharacterComicsFromNetwork().collect {
                comicsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.getCharacterEventsFromNetwork().collect {
                eventsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.getCharacterSeriesFromNetwork().collect {
                seriesAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.getCharacterEventsFromNetwork().collect {
                storiesAdapter.submitData(it)
            }
        }
    }

    override fun onItemClick(item: Any) {
        item as ResultsItem
        val url = item.thumbnail.path + "." + item.thumbnail.extension
        viewModel.setSelectedItemThumbnail(url)
        val dialog = DisplayImageDialog()
        dialog.show(supportFragmentManager, dialog.tag)
    }

    companion object {
        private const val TAG = "CharacterDetailsActivit"
    }
}