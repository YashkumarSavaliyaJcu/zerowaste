package com.yash026.zerowaste.model

data class MealResponse(
    val meals: List<Meal>
)

data class Meal(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String
)
