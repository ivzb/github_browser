package com.ivzb.github_browser.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivzb.github_browser.databinding.FragmentUserProfileBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.ui.user_profile.UserProfileFragmentDirections.Companion.toRepos
import com.ivzb.github_browser.ui.user_profile.UserProfileFragmentDirections.Companion.toUsers
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.provideViewModel
import com.ivzb.github_browser.util.updateTitle
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val userProfileViewModel: UserProfileViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentUserProfileBinding =
            FragmentUserProfileBinding.inflate(inflater, container, false).apply {
                viewModel = userProfileViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        userProfileViewModel.user.observe(viewLifecycleOwner, EventObserver { user ->
            user?.let { updateTitle("${user.login} profile") }
            binding.user = user
        })

        userProfileViewModel.loading.observe(viewLifecycleOwner, EventObserver {
            binding.loading = it
        })

        userProfileViewModel.error.observe(viewLifecycleOwner, EventObserver {
            binding.error = it
        })

        userProfileViewModel.userProfileEvent.observe(viewLifecycleOwner, EventObserver {
            val (event, user) = it

            when (event) {
                UserProfileEvent.Repositories -> openRepositories(user)
                UserProfileEvent.Starred -> openStarred(user)
                UserProfileEvent.Following -> openFollowing(user)
                UserProfileEvent.Followers -> openFollowers(user)
            }.checkAllMatched
        })

        requireArguments().apply {
            val user = UserProfileFragmentArgs.fromBundle(this).user
            binding.name = user
            userProfileViewModel.getUser(user)
        }

        return binding.root
    }

    private fun openRepositories(user: String) =
        findNavController().navigate(toRepos(user, RepoType.Own))

    private fun openStarred(user: String) =
        findNavController().navigate(toRepos(user, RepoType.Starred))

    private fun openFollowing(user: String) =
        findNavController().navigate(toUsers(user, UserType.Following))

    private fun openFollowers(user: String) =
        findNavController().navigate(toUsers(user, UserType.Followers))
}
