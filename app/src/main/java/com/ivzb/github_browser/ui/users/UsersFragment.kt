package com.ivzb.github_browser.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.github_browser.databinding.FragmentUsersBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.model.ui.User
import com.ivzb.github_browser.ui.*
import com.ivzb.github_browser.ui.users.UsersFragmentDirections.Companion.toUserProfile
import com.ivzb.github_browser.util.provideViewModel
import com.ivzb.github_browser.util.updateTitle
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UsersFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var usersViewModel: UsersViewModel

    private var adapter: ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        usersViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentUsersBinding = FragmentUsersBinding.inflate(inflater, container, false).apply {
            viewModel = usersViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        usersViewModel.users.observe(viewLifecycleOwner, Observer {
            showUsers(binding.rvUsers, it)
        })

        usersViewModel.click.observe(viewLifecycleOwner, EventObserver {
            openUserProfile(it)
        })

        requireArguments().apply {
            val (user, type) = UsersFragmentArgs.fromBundle(this)
            binding.user = user
            binding.type = type

            usersViewModel.getUsers(user, type)

            updateTitle("$user ${type.toString().toLowerCase()}")
        }

        return binding.root
    }

    private fun showUsers(recyclerView: RecyclerView, list: List<Any>) {
        if (adapter == null) {
            adapter = createAdapter()
        }

        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }

        (recyclerView.adapter as ItemAdapter).submitList(list)
    }

    private fun createAdapter(): ItemAdapter {
        val userViewBinder = UserViewBinder(this, usersViewModel)
        val emptyViewBinder = EmptyViewBinder()
        val noConnectionViewBinder = NoConnectionViewBinder()
        val viewBinders = HashMap<ItemClass, ItemBinder>().apply {
            put(userViewBinder.modelClass, userViewBinder as ItemBinder)
            put(emptyViewBinder.modelClass, emptyViewBinder as ItemBinder)
            put(noConnectionViewBinder.modelClass, noConnectionViewBinder as ItemBinder)
        }

        return ItemAdapter(viewBinders)
    }

    private fun openUserProfile(user: User) =
        findNavController().navigate(toUserProfile(user.login))
}
