package com.maxi.dailyfeed.presentation.news.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.maxi.dailyfeed.R
import com.maxi.dailyfeed.databinding.ActivityNewsBinding
import com.maxi.dailyfeed.presentation.base.UiEvent
import com.maxi.dailyfeed.presentation.base.UiState
import com.maxi.dailyfeed.presentation.news.adapter.NewsAdapter
import com.maxi.dailyfeed.presentation.news.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    @Inject
    lateinit var adapter: NewsAdapter

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        observeDataAndUpdateUi()
    }

    private fun setupUi() {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = this@NewsActivity.adapter
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@NewsActivity,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    private fun observeDataAndUpdateUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                adapter.submitList(state.data)
                                updateProgressBarVisibility(false)
                            }

                            is UiState.Loading -> {
                                Log.d(TAG, "observeDataAndUpdateUi: Loading!")
                                updateProgressBarVisibility(true)
                            }
                        }
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is UiEvent.Error -> {
                                Log.e(TAG, "observeDataAndUpdateUi: Error Event: ${event.error} ")
                                showErrorMessage(event.error)
                                updateProgressBarVisibility(false)
                            }

                            is UiEvent.RefreshStarted -> {
                                Log.d(TAG, "observeDataAndUpdateUi: RefreshStarted Event!")
                            }

                            is UiEvent.RefreshComplete -> {
                                Log.d(TAG, "observeDataAndUpdateUi: RefreshCompleted Event!")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateProgressBarVisibility(isVisible: Boolean) {
        binding.pbNews.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(message: String) {
        Snackbar
            .make(
                binding.root,
                message,
                Snackbar.LENGTH_LONG
            )
            .show()
    }
}

private const val TAG = "MainActivityTAG"