package com.ivzb.github_browser.ui.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.databinding.FragmentReposBinding
import com.ivzb.github_browser.util.provideViewModel
import com.ivzb.github_browser.util.updateTitle
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ReposFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val reposViewModel: ReposViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentReposBinding = FragmentReposBinding.inflate(inflater, container, false).apply {
            viewModel = reposViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        requireArguments().apply {
            val user = ReposFragmentArgs.fromBundle(this).user
            binding.user = user
            reposViewModel.getRepos(user)

            updateTitle("$user repos")
        }

        return binding.root
    }
}
