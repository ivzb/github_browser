package com.ivzb.github_browser.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.R
import com.ivzb.github_browser.util.provideViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel = provideViewModel(viewModelFactory)

        setContentView(R.layout.activity_main)
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}