package com.ivzb.github_browser.ui.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.github_browser.databinding.FragmentReposBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.model.ui.Repo
import com.ivzb.github_browser.ui.*
import com.ivzb.github_browser.ui.repos.ReposFragmentDirections.Companion.toRepoProfile
import com.ivzb.github_browser.util.provideViewModel
import com.ivzb.github_browser.util.updateTitle
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ReposFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var reposViewModel: ReposViewModel

    private var adapter: ItemAdapter? = null

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

        reposViewModel.repos.observe(viewLifecycleOwner, Observer {
            showRepos(binding.rvRepos, it)
        })

        reposViewModel.click.observe(viewLifecycleOwner, EventObserver {
            openRepoProfile(it)
        })

        requireArguments().apply {
            val user = ReposFragmentArgs.fromBundle(this).user
            binding.user = user
            reposViewModel.getRepos(user)

            updateTitle("$user repos")
        }

        return binding.root
    }

    private fun showRepos(recyclerView: RecyclerView, list: List<Any>) {
        if (adapter == null) {
            adapter = createAdapter()
        }

        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }

        (recyclerView.adapter as ItemAdapter).submitList(list)
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

        return ItemAdapter(viewBinders)
    }

    private fun openRepoProfile(repo: Repo) =
        findNavController().navigate(toRepoProfile(repo.fullName))
}
