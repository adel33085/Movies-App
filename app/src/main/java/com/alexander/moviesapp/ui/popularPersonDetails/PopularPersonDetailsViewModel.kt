package com.alexander.moviesapp.ui.popularPersonDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexander.domain.entity.PopularPersonDetails
import com.alexander.domain.entity.RequestResult
import com.alexander.domain.repo.IMoviesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopularPersonDetailsViewModel(
    private val moviesRepo: IMoviesRepo
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getPopularPersonDetails(personId: Int): LiveData<PopularPersonDetails> {
        loading.value = true
        val data: MutableLiveData<PopularPersonDetails> = MutableLiveData()
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { moviesRepo.getPopularPersonDetails(personId) }) {
                is RequestResult.Success -> {
                    loading.value = false
                    data.value = result.data
                    error.value = null
                }
                is RequestResult.Error -> {
                    loading.value = false
                    data.value = null
                    error.value = result.exception.message
                }
            }
        }
        return data
    }
}
