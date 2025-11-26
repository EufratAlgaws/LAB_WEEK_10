package com.example.lab_week_10

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel: ViewModel() {

    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    // Initialize the LiveData object
    init {
        _total.postValue(0)
    }

    // Increment the total value
    fun incrementTotal() {
        _total.postValue((_total.value ?: 0) + 1)
    }


    fun setTotal(newTotal: Int) {
        _total.postValue(newTotal)
    }
}
