package com.alexander.moviesapp.ui.repeatedActors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alexander.domain.entity.Actor
import com.alexander.moviesapp.R
import com.alexander.moviesapp.helpers.ScreenUtility
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.repeated_actor_list_item.view.*
import kotlin.math.roundToInt

class RepeatedActorsAdapter(
    private val context: Context,
    private val screenUtility: ScreenUtility,
    private val clickListener: OnActorClickListener
) : ListAdapter<Actor, RepeatedActorViewHolder>(DiffCallback1) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatedActorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repeated_actor_list_item, parent, false)
        return RepeatedActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepeatedActorViewHolder, position: Int) {
        holder.bind(context, screenUtility, clickListener, getItem(position))
    }
}

object DiffCallback1 : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.actorId == newItem.actorId
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}

interface OnActorClickListener {
    fun onActorClick(actor: Actor)
}

class RepeatedActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(context: Context, screenUtility: ScreenUtility, clickListener: OnActorClickListener, actor: Actor) {
        itemView.repeatedActorListItem.layoutParams?.width = getItemWidth(context, screenUtility)
        itemView.repeatedActorListItem.layoutParams?.height = getItemHeight(context, screenUtility)
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 10f
        circularProgressDrawable.start()
        Glide.with(itemView.actorPicImgV)
            .load(actor.actorPic)
            .placeholder(circularProgressDrawable)
            .into(itemView.actorPicImgV)
        itemView.setOnClickListener {
            clickListener.onActorClick(actor)
        }
    }

    private fun getItemWidth(context: Context, screenUtility: ScreenUtility): Int {
        val dpWidth = screenUtility.width
        return dpToPx(context, dpWidth) / 2
    }

    private fun getItemHeight(context: Context, screenUtility: ScreenUtility): Int {
        val dpHeight = screenUtility.height
        return dpToPx(context, dpHeight) / 2
    }

    private fun dpToPx(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
}
