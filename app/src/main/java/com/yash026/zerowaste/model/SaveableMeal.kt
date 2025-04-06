package com.yash026.zerowaste.model

sealed interface SaveableMeal {

    val id: String
    val isSaved: Boolean
}