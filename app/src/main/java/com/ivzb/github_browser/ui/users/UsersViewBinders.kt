package com.ivzb.github_browser.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.ItemUserBinding
import com.ivzb.github_browser.model.ui.User
import com.ivzb.github_browser.ui.ItemViewBinder

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
