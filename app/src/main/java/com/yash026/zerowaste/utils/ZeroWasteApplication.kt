package com.yash026.zerowaste.utils

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.yash026.zerowaste.database.ListItemDatabase
import com.yash026.zerowaste.viewmodels.ListItemRepository
import kotlin.getValue

class ZeroWasteApplication : Application() {
    val database by lazy { ListItemDatabase.Companion.getDatabase(this) }
    val repository by lazy { ListItemRepository(database.listItemDao()) }

    override fun onCreate() {
        super.onCreate()

        mInstance = this

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

        WorkManager.initialize(this, config)

    }

    companion object {
        lateinit var mInstance: ZeroWasteApplication
        fun getContext(): Context? {
            return mInstance.applicationContext
        }
    }

}