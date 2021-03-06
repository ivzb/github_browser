package com.ivzb.github_browser.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.ItemUserBinding
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.ui.ItemViewBinder
import com.ivzb.github_browser.ui.QueryMatcher

class UserViewBinder(
    private val lifecycleOwner: LifecycleOwner,
    private val usersViewModel: UsersViewModel
) : ItemViewBinder<User, UserViewHolder>(
    User::class.java
) {

    override fun createViewHolder(parent: ViewGroup): UserViewHolder =
        UserViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            lifecycleOwner,
            usersViewModel
        )

    override fun bindViewHolder(model: User, viewHolder: UserViewHolder) {
        viewHolder.bind(model)
    }

    override fun getItemType(): Int = R.layout.item_user

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val usersViewModel: UsersViewModel
) : ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.user = user
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()

        binding.cvUser.setOnClickListener {
            usersViewModel.click(user)
        }
    }
}

class UsersQueryMatcher : QueryMatcher {

    override fun matches(item: Any, query: String): Boolean {
        if (item is User) {
            listOf(item.login, item.name)
                .forEach { if (it.contains(query, ignoreCase = true)) return true }

            return false
        }

        return true
    }
}
