package com.ivzb.github_browser.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.FragmentUserProfileBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.ui.repos.ReposType
import com.ivzb.github_browser.ui.user_profile.UserProfileFragmentDirections.Companion.toRepos
import com.ivzb.github_browser.ui.user_profile.UserProfileFragmentDirections.Companion.toUsers
import com.ivzb.github_browser.ui.users.UsersType
import com.ivzb.github_browser.util.provideViewModel
import com.ivzb.github_browser.util.showErrorMessage
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

        userProfileViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let { updateTitle("${user.login} profile") }
        })

        userProfileViewModel.error.observe(viewLifecycleOwner, Observer {
            showErrorMessage(it.peekContent())
        })

        userProfileViewModel.userProfileEvent.observe(viewLifecycleOwner, EventObserver {
            val (event, user) = it

            when (event) {
                UserProfileEvent.Repositories -> openRepositories(user)
                UserProfileEvent.Starred -> openStarred(user)
                UserProfileEvent.Following -> openFollowing(user)
                UserProfileEvent.Followers -> openFollowers(user)
            }
        })

        requireArguments().apply {
            val user = UserProfileFragmentArgs.fromBundle(this).user

            userProfileViewModel.getUser(user)
        }

        return binding.root
    }

    private fun openRepositories(user: String) =
        findNavController().navigate(toRepos(user, ReposType.Own))

    private fun openStarred(user: String) =
        findNavController().navigate(toRepos(user, ReposType.Starred))

    private fun openFollowing(user: String) =
        findNavController().navigate(toUsers(user, UsersType.Following))

    private fun openFollowers(user: String) =
        findNavController().navigate(toUsers(user, UsersType.Followers))
}
