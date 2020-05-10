package com.ivzb.github_browser.ui.users

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.FragmentUsersBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.ui.*
import com.ivzb.github_browser.ui.users.UsersFragmentDirections.Companion.toUserProfile
import com.ivzb.github_browser.util.*
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UsersFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var usersViewModel: UsersViewModel
    private lateinit var title: String

    private var adapter: ItemAdapter? = null
    private var searchItem: MenuItem? = null

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
            closeSearch(searchItem)
            clearSearch(searchItem)
        })

        usersViewModel.searchQuery.observe(viewLifecycleOwner, Observer {
            filterUsers(it)
        })

        setHasOptionsMenu(true)

        requireArguments().apply {
            val (user, type) = UsersFragmentArgs.fromBundle(this)
            binding.user = user
            binding.type = type

            usersViewModel.getUsers(user, type)

            title = "$user ${type.toString().toLowerCase()}"
            updateTitle(title)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)

        createSearchMenu(menu, menuInflater, object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                usersViewModel.search(query)
                updateTitle(title, query)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                closeSearch(searchItem)
                return true
            }
        })

        searchItem = menu.findItem(R.id.search)
    }

    private fun showUsers(recyclerView: RecyclerView, list: List<Any>) {
        if (adapter == null) {
            adapter = createAdapter()
        }

        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }

        (recyclerView.adapter as ItemAdapter).setList(list)
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
        val queryMatcher = UsersQueryMatcher()

        return ItemAdapter(viewBinders, queryMatcher)
    }

    private fun openUserProfile(user: User) =
        findNavController().navigate(toUserProfile(user.login))

    private fun filterUsers(query: String) =
        adapter?.filter?.filter(query)
}
