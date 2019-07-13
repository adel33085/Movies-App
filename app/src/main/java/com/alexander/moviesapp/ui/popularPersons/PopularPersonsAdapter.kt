package com.alexander.moviesapp.ui.popularPersons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexander.data.remote.NetworkState
import com.alexander.data.remote.Status
import com.alexander.domain.entity.PopularPerson
import com.alexander.moviesapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.network_state_list_item.view.*
import kotlinx.android.synthetic.main.popular_person_list_item.view.*

class PopularPersonsAdapter(
    private val clickListener: OnPopularPersonClickListener,
    private val retryCallback: () -> Unit
) : PagedListAdapter<PopularPerson, RecyclerView.ViewHolder>(DiffCallback) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.popular_person_list_item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_person_list_item, parent, false)
                PopularPersonViewHolder(view)
            }
            R.layout.network_state_list_item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.network_state_list_item, parent, false)
                NetworkStateViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("unknown view type $viewType")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_list_item
        } else {
            R.layout.popular_person_list_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.popular_person_list_item -> {
                (holder as PopularPersonViewHolder).bind(clickListener, getItem(position))
            }
            R.layout.network_state_list_item -> {
                (holder as NetworkStateViewHolder).bind(retryCallback, networkState)
            }
        }
    }
}

object DiffCallback : DiffUtil.ItemCallback<PopularPerson>() {
    override fun areItemsTheSame(oldItem: PopularPerson, newItem: PopularPerson): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PopularPerson, newItem: PopularPerson): Boolean {
        return oldItem == newItem
    }
}

interface OnPopularPersonClickListener {
    fun onPopularPersonClick(person: PopularPerson)
}

class PopularPersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(clickListener: OnPopularPersonClickListener, person: PopularPerson?) {
        if (person != null) {
            Glide.with(itemView.personPicImageView)
                .load(person.profilePic)
                .circleCrop()
                .placeholder(R.drawable.ic_person_image_placeholder)
                .into(itemView.personPicImageView)
            itemView.personNameTextView.text = person.name
            itemView.setOnClickListener {
                clickListener.onPopularPersonClick(person)
            }
        }
    }
}

class NetworkStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(retryCallback: () -> Unit, networkState: NetworkState?) {
        itemView.loadingIndicator.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
        itemView.retryButton.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        itemView.errorMessageTextView.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
        itemView.errorMessageTextView.text = networkState?.message
        itemView.retryButton.setOnClickListener {
            retryCallback()
        }
    }
}
