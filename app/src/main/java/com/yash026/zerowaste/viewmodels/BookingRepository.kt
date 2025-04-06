package com.yash026.zerowaste.viewmodels

import androidx.lifecycle.LiveData
import com.yash026.zerowaste.database.BookingDao
import com.yash026.zerowaste.model.Booking

class BookingRepository(private val bookingDao: BookingDao) {

    val allBookings: LiveData<List<Booking>> = bookingDao.getAllBookings()

    suspend fun insert(booking: Booking): Long {
        return bookingDao.insert(booking)
    }

    suspend fun getBookingById(id: Long): Booking? {
        return bookingDao.getBookingById(id)
    }

    suspend fun update(booking: Booking) {
        bookingDao.update(booking)
    }

    suspend fun delete(booking: Booking) {
        bookingDao.deleteBooking(booking)
    }
}
