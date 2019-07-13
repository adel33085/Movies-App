package com.alexander.moviesapp.ui.popularPersonDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alexander.domain.entity.PopularPersonDetails
import com.alexander.moviesapp.R
import com.alexander.moviesapp.ui.fullScreenImage.FullScreenImageActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.network_state_list_item.*
import kotlinx.android.synthetic.main.popular_person_details_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class PopularPersonDetailsActivity : AppCompatActivity(), OnPersonImageClickListener {

    private val viewModel: PopularPersonDetailsViewModel  by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popular_person_details_activity)

        val personId = intent.getIntExtra("key_person_id", 0)

        getPopularPersonDetails(personId)

        viewModel.loading.observe(this, Observer {
            if (it != null) {
                if (it) {
                    mainLayout.visibility = View.GONE
                    networkStateLayout.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    errorMessageTextView.visibility = View.GONE
                    retryButton.visibility = View.GONE
                } else {
                    networkStateLayout.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                mainLayout.visibility = View.GONE
                networkStateLayout.visibility = View.VISIBLE
                loadingIndicator.visibility = View.GONE
                errorMessageTextView.visibility = View.VISIBLE
                errorMessageTextView.text = it
                retryButton.visibility = View.VISIBLE
                retryButton.setOnClickListener {
                    getPopularPersonDetails(personId)
                }
            }
        })
    }

    private fun getPopularPersonDetails(personId: Int) {
        viewModel.getPopularPersonDetails(personId).observe(this, Observer {
            if (it != null) {
                mainLayout.visibility = View.VISIBLE
                networkStateLayout.visibility = View.GONE
                bind(it)
            }
        })
    }

    private fun bind(personDetails: PopularPersonDetails) {
        collapsingToolbar.title = personDetails.name
        Glide.with(personPicImgV)
            .load(personDetails.profilePic)
            .into(personPicImgV)
        if (personDetails.birthday != null && personDetails.placeOfBirth != null) {
            aboutTV.text = "${personDetails.biography} Born on ${personDetails.birthday} in ${personDetails.placeOfBirth}"
        } else {
            aboutTV.text = personDetails.biography
        }

        if (!personDetails.images.isNullOrEmpty()) {
            val layoutManager = GridLayoutManager(this, 2)
            personImagesRV.layoutManager = layoutManager
            val adapter = PersonImagesAdapter(personDetails.images!!, this, this)
            personImagesRV.adapter = adapter
        } else {
            personImagesRV.visibility = View.GONE
        }
    }

    override fun onPersonImageClick(imageUrl: String) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra("key_image_url", imageUrl)
        startActivity(intent)
    }
}
