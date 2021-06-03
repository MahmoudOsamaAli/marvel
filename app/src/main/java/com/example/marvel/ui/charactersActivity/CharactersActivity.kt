package com.example.marvel.ui.charactersActivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.*
import com.example.marvel.callBacks.OnCharacterClicked
import com.example.marvel.databinding.ActivityCharactersBinding
import com.example.marvel.model.characters.ResultsItem
import com.example.marvel.ui.charactersActivity.adapters.CharactersAdapter
import com.example.marvel.ui.charactersActivity.adapters.CharactersLoadStateAdapter
import com.example.marvel.ui.dialogs.SearchDialog
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.example.marvel.utils.Injection
import com.example.marvel.viewModels.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CharactersActivity : AppCompatActivity(), OnCharacterClicked {

    private lateinit var binding: ActivityCharactersBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, Injection.provideViewModelFactory())[MainViewModel::class.java]
    }
    private val adapter = CharactersAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_characters)
        init()
    }

    private fun init() {
        setToolBar()
        setClickListeners()
        initAdapter()
        getData()
        initLoading()
    }

    private fun initLoading() {
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading && adapter.itemCount > 0) {
                binding.animator.displayedChild = 1
                viewModel.charactersList.value = ArrayList(adapter.snapshot().items)
            }
            if (it.refresh is LoadState.Error) {
                binding.animator.displayedChild = 2
                binding.errorLayout.progressBar.isVisible = false
                binding.errorLayout.errorMsg.text = resources.getString(R.string.error_loading)
                binding.errorLayout.retryButton.setOnClickListener {
                    getData()
                }
            }
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            binding.animator.displayedChild = 0
            viewModel.loadDataFromNetwork().collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setClickListeners() {
        binding.searchButton.setOnClickListener {
            val dialog = SearchDialog()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }

    private fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = CharactersLoadStateAdapter { adapter.retry() },
            footer = CharactersLoadStateAdapter { adapter.retry() }
        )
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                binding.upButton.isVisible = dy <= 0
                binding.upButton.isVisible = position != 0
            }
        })
        binding.upButton.setOnClickListener { binding.list.smoothScrollToPosition(0) }
    }

    private fun setToolBar() {
        setNoLimitsWindow()
        setSupportActionBar(binding.toolbar)
    }

    override fun onItemClicked(item: ResultsItem, view: View) {
        viewModel.startCharactersDetailedActivity(item,view,this)
    }

}