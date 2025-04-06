package com.yash026.zerowaste.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash026.zerowaste.data.RetrofitInstance
import com.yash026.zerowaste.model.MealDetail
import kotlinx.coroutines.launch


class MealDetailViewModel(


) : ViewModel() {
    var meal by mutableStateOf<MealDetail?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set


    fun fetchMeal(mealId: String) {
        if (isLoading || (meal?.idMeal == mealId && meal != null)) {
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitInstance.api.getMealById(mealId)
                meal = response.meals?.firstOrNull()
                if (meal == null) {
                    error = "Meal not found."
                }
            } catch (e: Exception) {
                error = "Failed to load details: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }


}
