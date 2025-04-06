package com.yash026.zerowaste.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yash026.zerowaste.model.Booking


@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(booking: Booking) : Long

    @Query("SELECT * FROM booking_table ORDER BY id DESC")
    fun getAllBookings(): LiveData<List<Booking>>

    @Query("SELECT * FROM booking_table WHERE id = :id")
    suspend fun getBookingById(id: Long): Booking?

    @Update
    suspend fun update(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)

}
