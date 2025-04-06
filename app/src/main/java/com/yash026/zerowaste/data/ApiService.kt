package com.yash026.zerowaste.data

import com.yash026.zerowaste.model.MealDetailResponse
import com.yash026.zerowaste.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("filter.php")
    suspend fun getMeals(@Query("a") area: String): MealResponse

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse

    @GET("filter.php")
    suspend fun queryMealsByCategory(@Query("c") category: String): MealResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") mealId: String): MealDetailResponse


}
