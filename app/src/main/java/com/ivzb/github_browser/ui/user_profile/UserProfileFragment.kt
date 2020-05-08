package com.ivzb.github_browser.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }
}
