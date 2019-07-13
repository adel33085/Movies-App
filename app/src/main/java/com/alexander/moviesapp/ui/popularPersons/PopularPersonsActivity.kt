package com.alexander.moviesapp.ui.popularPersons

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alexander.data.remote.Status
import com.alexander.domain.entity.PopularPerson
import com.alexander.moviesapp.R
import com.alexander.moviesapp.ui.popularPersonDetails.PopularPersonDetailsActivity
import com.alexander.moviesapp.ui.search.SearchActivity
import kotlinx.android.synthetic.main.network_state_list_item.*
import kotlinx.android.synthetic.main.popular_persons_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularPersonsActivity : AppCompatActivity(), OnPopularPersonClickListener {

    private val viewModel: PopularPersonsViewModel  by viewModel()
    private val adapter by lazy { PopularPersonsAdapter(this, { viewModel.retry() }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popular_persons_activity)

        popularPersonsRV.adapter = adapter
        popularPersonsRV.setHasFixedSize(true)

        viewModel.popularPersonsList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.initialNetworkState.observe(this, Observer {
            mainLayout.visibility = if (it?.status == Status.SUCCESS) View.VISIBLE else View.GONE
            networkStateLayout.visibility = if (it?.status == Status.RUNNING || it?.status == Status.FAILED) View.VISIBLE else View.GONE
            loadingIndicator.visibility = if (it?.status == Status.RUNNING) View.VISIBLE else View.GONE
            retryButton.visibility = if (it?.status == Status.FAILED) View.VISIBLE else View.GONE
            errorMessageTextView.visibility = if (it?.message != null) View.VISIBLE else View.GONE
            errorMessageTextView.text = it?.message
            retryButton.setOnClickListener {
                viewModel.retry()
            }
        })

        searchFabButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPopularPersonClick(person: PopularPerson) {
        val intent = Intent(this, PopularPersonDetailsActivity::class.java)
        intent.putExtra("key_person_id", person.id)
        startActivity(intent)
    }
}
