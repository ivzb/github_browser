package com.ivzb.github_browser.ui.repos

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.FragmentReposBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.ui.*
import com.ivzb.github_browser.ui.repos.ReposFragmentDirections.Companion.toRepoProfile
import com.ivzb.github_browser.util.*
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ReposFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var reposViewModel: ReposViewModel
    private lateinit var title: String

    private var adapter: ItemAdapter? = null
    private var searchItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        reposViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentReposBinding = FragmentReposBinding.inflate(inflater, container, false).apply {
            viewModel = reposViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        reposViewModel.repos.observe(viewLifecycleOwner, EventObserver {
            showRepos(binding.rvRepos, it)
        })

        reposViewModel.click.observe(viewLifecycleOwner, EventObserver {
            openRepoProfile(it)
            closeSearch(searchItem)
            clearSearch(searchItem)
        })

        reposViewModel.searchQuery.observe(viewLifecycleOwner, EventObserver {
            filterRepos(it)
        })

        reposViewModel.loading.observe(viewLifecycleOwner, EventObserver {
            binding.loading = it
        })

        setHasOptionsMenu(true)

        requireArguments().apply {
            val (user, type) = ReposFragmentArgs.fromBundle(this)
            binding.user = user
            binding.type = type
            reposViewModel.getRepos(user, type)

            title = "$user ${type.toString().toLowerCase()} repos"
            updateTitle(title)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)

        createSearchMenu(menu, menuInflater, object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                reposViewModel.search(query)
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

    private fun showRepos(recyclerView: RecyclerView, list: List<Any>) {
        if (adapter == null) {
            adapter = createAdapter()
        }

        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }

        (recyclerView.adapter as ItemAdapter).setList(list)
    }

    private fun createAdapter(): ItemAdapter {
        val repoViewBinder = RepoViewBinder(this, reposViewModel)
        val emptyViewBinder = EmptyViewBinder()
        val noConnectionViewBinder = NoConnectionViewBinder()
        val viewBinders = HashMap<ItemClass, ItemBinder>().apply {
            put(repoViewBinder.modelClass, repoViewBinder as ItemBinder)
            put(emptyViewBinder.modelClass, emptyViewBinder as ItemBinder)
            put(noConnectionViewBinder.modelClass, noConnectionViewBinder as ItemBinder)
        }
        val queryMatcher = ReposQueryMatcher()

        return ItemAdapter(viewBinders, queryMatcher)
    }

    private fun openRepoProfile(repo: Repo) =
        findNavController().navigate(toRepoProfile(repo.fullName))

    private fun filterRepos(query: String) =
        adapter?.filter?.filter(query)
}
