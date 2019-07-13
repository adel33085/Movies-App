package com.alexander.moviesapp.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alexander.data.remote.Status
import com.alexander.domain.entity.PopularPerson
import com.alexander.moviesapp.R
import com.alexander.moviesapp.ui.popularPersonDetails.PopularPersonDetailsActivity
import com.alexander.moviesapp.ui.popularPersons.OnPopularPersonClickListener
import com.alexander.moviesapp.ui.popularPersons.PopularPersonsAdapter
import kotlinx.android.synthetic.main.network_state_list_item.*
import kotlinx.android.synthetic.main.search_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), OnPopularPersonClickListener {

    private val viewModel: SearchViewModel  by viewModel()
    private val adapter by lazy { PopularPersonsAdapter(this, { viewModel.retry() }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        popularPersonsRV.adapter = adapter
        popularPersonsRV.setHasFixedSize(true)

        searchTIET.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchKeywords = searchTIET.text?.trim().toString()
                    if (searchKeywords.isNotEmpty()) {
                        hideSoftKeyboard()
                        viewModel.getPopularPersons(searchKeywords)
                        viewModel.popularPersonsList.observe(this, Observer {
                            if (it.isNullOrEmpty()) {
                                popularPersonsRV.visibility = View.GONE
                                networkStateLayout.visibility = View.VISIBLE
                                retryButton.visibility = View.GONE
                            } else {
                                popularPersonsRV.visibility = View.VISIBLE
                                networkStateLayout.visibility = View.GONE
                                adapter.submitList(it)
                            }
                        })

                        viewModel.networkState.observe(this, Observer {
                            adapter.setNetworkState(it)
                        })

                        viewModel.initialNetworkState.observe(this, Observer {
                            popularPersonsRV.visibility = View.GONE
                            networkStateLayout.visibility = View.VISIBLE
                            loadingIndicator.visibility = if (it?.status == Status.RUNNING) View.VISIBLE else View.GONE
                            retryButton.visibility = if (it?.status == Status.FAILED) View.VISIBLE else View.GONE
                            errorMessageTextView.visibility = if (it?.message != null) View.VISIBLE else View.GONE
                            errorMessageTextView.text = it?.message
                            retryButton.setOnClickListener {
                                viewModel.retry()
                            }
                        })
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onPopularPersonClick(person: PopularPerson) {
        val intent = Intent(this, PopularPersonDetailsActivity::class.java)
        intent.putExtra("key_person_id", person.id)
        startActivity(intent)
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
