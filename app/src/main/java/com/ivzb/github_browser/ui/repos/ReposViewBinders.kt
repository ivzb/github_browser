package com.ivzb.github_browser.ui.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.ItemRepoBinding
import com.ivzb.github_browser.model.ui.Repo
import com.ivzb.github_browser.ui.ItemViewBinder
import com.ivzb.github_browser.ui.QueryMatcher

class RepoViewBinder(
    private val lifecycleOwner: LifecycleOwner,
    private val reposViewModel: ReposViewModel
) : ItemViewBinder<Repo, RepoViewHolder>(
    Repo::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RepoViewHolder =
        RepoViewHolder(
            ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            lifecycleOwner,
            reposViewModel
        )

    override fun bindViewHolder(model: Repo, viewHolder: RepoViewHolder) {
        viewHolder.bind(model)
    }

    override fun getItemType(): Int = R.layout.item_repo

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem == newItem
}

class RepoViewHolder(
    private val binding: ItemRepoBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val reposViewModel: ReposViewModel
) : ViewHolder(binding.root) {

    fun bind(repo: Repo) {
        binding.repo = repo
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()

        binding.cvRepo.setOnClickListener {
            reposViewModel.click(repo)
        }
    }
}

class ReposQueryMatcher : QueryMatcher {

    override fun matches(item: Any, query: String): Boolean {
        if (item is Repo) {
            listOf(item.name, item.fullName, item.description)
                .forEach { if (it.contains(query, ignoreCase = true)) return true }

            return false
        }

        return true
    }
}
