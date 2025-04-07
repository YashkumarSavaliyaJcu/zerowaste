package com.yash026.zerowaste.viewmodels

import androidx.lifecycle.LiveData
import com.yash026.zerowaste.database.ListItemDao
import com.yash026.zerowaste.model.Items

class ListItemRepository(private val listItemDao: ListItemDao) {

    val allBookings: LiveData<List<Items>> = listItemDao.getAllBookings()

    suspend fun insert(booking: Items): Long {
        return listItemDao.insert(booking)
    }

    suspend fun getBookingById(id: Long): Items? {
        return listItemDao.getBookingById(id)
    }

    suspend fun update(booking: Items) {
        listItemDao.update(booking)
    }

    suspend fun delete(booking: Items) {
        listItemDao.deleteBooking(booking)
    }
}
