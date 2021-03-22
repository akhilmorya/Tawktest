package com.tawktest.ui.views.activities.user_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.tawktest.R
import com.tawktest.app.utils.transformation.InvertTransformation
import com.tawktest.data.models.User
import com.tawktest.databinding.SingleItemUserBinding



class UserAdapter(private val users: MutableList<User>, val callback: (User) -> Unit) :
        RecyclerView.Adapter<UserAdapter.UserHolder>(), Filterable {

    private var mUsers = mutableListOf<User>()

    init {
        this.mUsers = users
    }

    inner class UserHolder(val binding: SingleItemUserBinding) :
            RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { callback(mUsers[layoutPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = DataBindingUtil.inflate<SingleItemUserBinding>(
                LayoutInflater.from(parent.context),
                R.layout.single_item_user,
                parent,
                false
        )
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.binding.civAvatar.load(mUsers[position].avatarUrl) {
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            if ((position + 1) % 4 == 0) { // 4th item should be inverted
                transformations(InvertTransformation())
            }
        }

        holder.binding.tvTitle.text = mUsers[position].login
        if (!mUsers[position].notes.isNullOrEmpty()) {
            holder.binding.ivNote.visibility = View.VISIBLE
        } else {
            holder.binding.ivNote.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = mUsers.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = FilterResults()
                if (!constraint.isNullOrEmpty()) {
                    constraint.let {
                        val s = it.toString()
                        val fUsers = mutableListOf<User>()
                        for (user in users) {
                            if (user.login.contains(
                                            s,
                                            true
                                    ) || (!user.notes.isNullOrEmpty() && user.notes!!.contains(s, true))
                            ) {
                                fUsers.add(user)
                            }
                        }
                        result.values = fUsers
                        result.count = fUsers.size
                    }
                } else {
                    result.values = users
                    result.count = users.size
                }
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    mUsers = it.values as ArrayList<User>
                    notifyDataSetChanged()
                }
            }
        }
    }
}