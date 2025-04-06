package com.yash026.zerowaste.utils

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.yash026.zerowaste.database.BookingDatabase
import com.yash026.zerowaste.viewmodels.BookingRepository
import kotlin.getValue

class BookingApplication : Application() {
    val database by lazy { BookingDatabase.Companion.getDatabase(this) }
    val repository by lazy { BookingRepository(database.bookingDao()) }

    override fun onCreate() {
        super.onCreate()

        mInstance = this

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

        WorkManager.initialize(this, config)

    }

    companion object {
        lateinit var mInstance: BookingApplication
        fun getContext(): Context? {
            return mInstance.applicationContext
        }
    }

}