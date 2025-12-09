package com.maxi.dailyfeed.presentation.news.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(
                    this, "Notifications enabled",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    this, "Notifications disabled",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

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
        askNotificationPermission()
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

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "Permission already granted!")
                }

                shouldShowRequestPermissionRationale(
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    AlertDialog.Builder(this)
                        .setTitle("Notification Permission Required")
                        .setMessage("We send updates when news is refreshed.")
                        .setPositiveButton("Allow") { _, _ ->
                            requestNotificationPermission.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }

                else -> {
                    // Directly ask for permission
                    requestNotificationPermission.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
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