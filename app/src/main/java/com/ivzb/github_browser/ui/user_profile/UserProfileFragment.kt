package com.ivzb.github_browser.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.databinding.FragmentUserProfileBinding
import com.ivzb.github_browser.util.provideViewModel
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

        val binding: FragmentUserProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false).apply {
            viewModel = userProfileViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        userProfileViewModel.user.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(requireContext(), "User loaded", Toast.LENGTH_LONG).show()
            }
        })

        userProfileViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showErrorMessage(it.peekContent())
        })

        userProfileViewModel.getUser(null)

        return binding.root
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
