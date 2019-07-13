package com.alexander.moviesapp.ui.repeatedActors

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alexander.domain.entity.Actor
import com.alexander.moviesapp.R
import com.alexander.moviesapp.helpers.ScreenUtility
import com.alexander.moviesapp.ui.popularPersonDetails.PopularPersonDetailsActivity
import kotlinx.android.synthetic.main.network_state_list_item.*
import kotlinx.android.synthetic.main.repeated_actors_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepeatedActorsActivity : AppCompatActivity(), OnActorClickListener {

    private val viewModel: RepeatedActorsViewModel  by viewModel()
    private val adapter by lazy { RepeatedActorsAdapter(this, ScreenUtility(this), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repeated_actors_activity)

        val layoutManager = GridLayoutManager(this, 2)
        repeatedActorsRV.layoutManager = layoutManager
        repeatedActorsRV.adapter = adapter

        getRepeatedActors()

        viewModel.loading.observe(this, Observer {
            if (it != null) {
                if (it) {
                    repeatedActorsRV.visibility = View.GONE
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
                repeatedActorsRV.visibility = View.GONE
                networkStateLayout.visibility = View.VISIBLE
                loadingIndicator.visibility = View.GONE
                errorMessageTextView.visibility = View.VISIBLE
                errorMessageTextView.text = it
                retryButton.visibility = View.VISIBLE
                retryButton.setOnClickListener {
                    getRepeatedActors()
                }
            }
        })
    }

    private fun getRepeatedActors() {
        viewModel.getRepeatedActors().observe(this, Observer {
            repeatedActorsRV.visibility = View.VISIBLE
            networkStateLayout.visibility = View.GONE
            adapter.submitList(it)
        })
    }

    override fun onActorClick(actor: Actor) {
        val intent = Intent(this, PopularPersonDetailsActivity::class.java)
        intent.putExtra("key_person_id", actor.actorId)
        startActivity(intent)
    }
}
