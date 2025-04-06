package com.yash026.zerowaste.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking_table")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val time: String,
    val image: String,
    val title: String,
    val description: String
)