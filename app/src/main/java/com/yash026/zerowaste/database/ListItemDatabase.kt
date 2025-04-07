package com.yash026.zerowaste.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yash026.zerowaste.model.Items

@Database(entities = [Items::class], version = 1, exportSchema = false)
abstract class ListItemDatabase : RoomDatabase() {

    abstract fun listItemDao(): ListItemDao

    companion object {
        @Volatile
        private var INSTANCE: ListItemDatabase? = null

        fun getDatabase(context: Context): ListItemDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ListItemDatabase::class.java,
                        "booking_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}
