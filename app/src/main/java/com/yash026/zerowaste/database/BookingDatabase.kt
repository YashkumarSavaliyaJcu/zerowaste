package com.yash026.zerowaste.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yash026.zerowaste.model.Booking

@Database(entities = [Booking::class], version = 1, exportSchema = false)
abstract class BookingDatabase : RoomDatabase() {

    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: BookingDatabase? = null

        fun getDatabase(context: Context): BookingDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookingDatabase::class.java,
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
