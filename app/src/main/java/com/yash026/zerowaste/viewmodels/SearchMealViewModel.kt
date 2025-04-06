package com.yash026.zerowaste.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash026.zerowaste.data.RetrofitInstance
import com.yash026.zerowaste.model.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchMealViewModel() : ViewModel() {


    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun fetchMeals(category: String) {

        Log.i("TAG###", "fetchMeals: $category")

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitInstance.api.searchMeals(category)
                _meals.value = response.meals ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to fetch meals: ${e.localizedMessage}"
                e.printStackTrace()
                _meals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchCategoryMeals(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitInstance.api.queryMealsByCategory(category)
                _meals.value = response.meals ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to fetch meals: ${e.localizedMessage}"
                e.printStackTrace()
                _meals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


}
