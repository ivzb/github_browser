package com.ivzb.github_browser.ui.search

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.FragmentSearchBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.ui.search.SearchFragmentDirections.Companion.toRepos
import com.ivzb.github_browser.ui.search.SearchFragmentDirections.Companion.toUsers
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.util.*
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchViewModel: SearchViewModel

    private var searchItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        searchViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentSearchBinding =
            FragmentSearchBinding.inflate(inflater, container, false).apply {
                viewModel = searchViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        searchViewModel.searchEvent.observe(viewLifecycleOwner, EventObserver {
            closeSearch(searchItem)
            clearSearch(searchItem)

            val (event, search) = it

            when (event) {
                SearchEvent.Repos -> openRepositories(search)
                SearchEvent.Users -> openUsers(search)
            }.checkAllMatched
        })

        setHasOptionsMenu(true)
        updateTitle(R.string.search)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)

        createSearchMenu(menu, menuInflater, object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                searchViewModel.search(query)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                closeSearch(searchItem)
                return true
            }
        })

        searchItem = menu.findItem(R.id.search)
    }

    private fun openRepositories(search: String) =
        findNavController().navigate(toRepos(search, RepoType.Search))

    private fun openUsers(search: String) =
        findNavController().navigate(toUsers(search, UserType.Search))
}
