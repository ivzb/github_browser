package com.ivzb.github_browser.ui.repo_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivzb.github_browser.databinding.FragmentRepoProfileBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.ui.user_profile.UserProfileFragmentDirections.Companion.toUsers
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.ui.users.UsersFragmentDirections.Companion.toUserProfile
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.provideViewModel
import com.ivzb.github_browser.util.showErrorMessage
import com.ivzb.github_browser.util.updateTitle
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RepoProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repoProfileViewModel: RepoProfileViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentRepoProfileBinding =
            FragmentRepoProfileBinding.inflate(inflater, container, false).apply {
                viewModel = repoProfileViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        repoProfileViewModel.repo.observe(viewLifecycleOwner, EventObserver { repo ->
            repo?.let { updateTitle("${repo.name} repo") }
            binding.repo = repo
        })

        repoProfileViewModel.error.observe(viewLifecycleOwner, EventObserver {
            showErrorMessage(it)
        })

        repoProfileViewModel.repoProfileEvent.observe(viewLifecycleOwner, EventObserver{
            val (event, value) = it

            when (event) {
                RepoProfileEvent.Contributors -> openContributors(value)
                RepoProfileEvent.Owner -> openOwner(value)
            }.checkAllMatched
        })

        requireArguments().apply {
            val repo = RepoProfileFragmentArgs.fromBundle(this).repo

            repoProfileViewModel.getRepo(repo)
        }

        return binding.root
    }

    private fun openContributors(repo: String) =
        findNavController().navigate(toUsers(repo, UserType.Contributors))

    private fun openOwner(user: String) =
        findNavController().navigate(toUserProfile(user))
}
