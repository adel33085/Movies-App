package com.alexander.moviesapp.ui.repeatedActors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexander.domain.entity.Actor
import com.alexander.domain.entity.RequestResult
import com.alexander.domain.repo.IMoviesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepeatedActorsViewModel(private val moviesRepo: IMoviesRepo) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getRepeatedActors(): LiveData<List<Actor>> {
        loading.value = true
        val data: MutableLiveData<List<Actor>> = MutableLiveData()
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { moviesRepo.getRepeatedActors() }) {
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
