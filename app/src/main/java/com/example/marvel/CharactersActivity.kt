package com.example.marvel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.databinding.ActivityCharactersBinding
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.example.marvel.utils.Injection
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class CharactersActivity : AppCompatActivity(){

    private lateinit var binding: ActivityCharactersBinding
    private val viewModel: MainViewModel by lazy{
        ViewModelProvider(this, Injection.provideViewModelFactory())[MainViewModel::class.java]
    }
    private val adapter = CharactersAdapter()
    private var loadDataJob: Job? = null

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
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }

            adapter.addLoadStateListener {
                if (it.refresh is LoadState.NotLoading && adapter.itemCount > 0){
                    Log.i(TAG, "initLoading: ")
                    binding.animator.displayedChild = 1
                }
                if (it.refresh is LoadState.Error){
                    Log.i(TAG, "initLoading: error")
                    binding.animator.displayedChild = 2
                    binding.errorLayout.progressBar.isVisible = false
                    binding.errorLayout.errorMsg.text = resources.getString(R.string.error_loading)
                    binding.errorLayout.retryButton.setOnClickListener {
                        getData()
                    }
                }
            }
        }
    }

    private fun getData() {
        loadDataJob = lifecycleScope.launch {
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
                binding.upButton.isVisible =  position != 0
            }
        })
        binding.upButton.setOnClickListener { binding.list.smoothScrollToPosition(0) }
    }

    private fun setToolBar() {
        setNoLimitsWindow()
        setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: adapter = $adapter")
    }

    companion object{
        private const val TAG = "CharactersActivity"
    }
}