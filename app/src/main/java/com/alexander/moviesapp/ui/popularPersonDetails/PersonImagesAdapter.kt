package com.alexander.moviesapp.ui.popularPersonDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alexander.moviesapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.person_image_list_item.view.*

class PersonImagesAdapter(
    private val imageUrls: List<String>,
    private val context: Context,
    private val clickListener: OnPersonImageClickListener
) : RecyclerView.Adapter<PersonImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_image_list_item, parent, false)
        return PersonImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: PersonImageViewHolder, position: Int) {
        holder.bind(imageUrls[position], context, clickListener)
    }
}

interface OnPersonImageClickListener {
    fun onPersonImageClick(imageUrl: String)
}

class PersonImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(imageUrl: String, context: Context, clickListener: OnPersonImageClickListener) {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 10f
        circularProgressDrawable.start()
        Glide.with(itemView.personImageImgV)
            .load(imageUrl)
            .placeholder(circularProgressDrawable)
            .into(itemView.personImageImgV)
        itemView.setOnClickListener {
            clickListener.onPersonImageClick(imageUrl)
        }
    }
}
