package com.yash026.zerowaste.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yash026.zerowaste.model.Items


@Dao
interface ListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(booking: Items) : Long

    @Query("SELECT * FROM booking_table ORDER BY id DESC")
    fun getAllBookings(): LiveData<List<Items>>

    @Query("SELECT * FROM booking_table WHERE id = :id")
    suspend fun getBookingById(id: Long): Items?

    @Update
    suspend fun update(booking: Items)

    @Delete
    suspend fun deleteBooking(booking: Items)

}
