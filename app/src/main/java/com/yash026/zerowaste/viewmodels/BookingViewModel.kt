package com.yash026.zerowaste.viewmodels

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash026.zerowaste.model.Booking
import com.yash026.zerowaste.notification.Notification
import com.yash026.zerowaste.utils.BookingApplication
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    val allBookings: LiveData<List<Booking>> = repository.allBookings

    companion object {
        const val titleExtra = "titleExtra"
        const val messageExtra = "messageExtra"
        const val notificationIdExtra = "notificationIdExtra"
    }


    fun insertAndSchedule(booking: Booking) = viewModelScope.launch {
        if (booking.id != 0) {
            Log.w(
                "TAG###",
                "insertAndSchedule called with a booking that already has an ID (${booking.id}). Consider using update logic."
            )
        }

        val insertedRowId: Long = repository.insert(booking)

        if (insertedRowId > 0) {
            Log.d("TAG###", "Booking inserted with row ID: $insertedRowId")
            val insertedBooking = repository.getBookingById(insertedRowId)

            if (insertedBooking != null) {
                Log.i(
                    "TAG###",
                    "Scheduling notification for newly inserted booking with ID: ${insertedBooking.id}"
                )
                scheduleNotification(insertedBooking)
            } else {
                Log.e(
                    "TAG###",
                    "Booking inserted (rowId $insertedRowId) but failed to retrieve it immediately for scheduling."
                )
            }
        } else {
            Log.e(
                "TAG###",
                "Database insertion failed (returned row ID: $insertedRowId). Notification not scheduled."
            )
        }
    }


    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(book: Booking) {
        val appContext = BookingApplication.mInstance

        if (book.id == 0) {
            Log.e("TAG###", "FATAL: scheduleNotification called with booking ID 0 unexpectedly.")
            return
        }
        val uniqueNotificationID: Int = book.id


        Log.i(
            "TAG###",
            "Preparing alarm with Notification ID: $uniqueNotificationID for Booking ID: ${book.id}"
        )

        val intent = Intent(appContext, Notification::class.java).apply {
            putExtra(titleExtra, book.title)
            putExtra(messageExtra, "Reminder for ${book.description}")
            putExtra(notificationIdExtra, uniqueNotificationID)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            uniqueNotificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w("TAG###", "Cannot schedule exact alarms. Permission missing or denied.")
                return
            }
        }

        val triggerTimeMillis = getTime(book)

        if (triggerTimeMillis != -1L && triggerTimeMillis > System.currentTimeMillis()) {
            Log.i(
                "TAG###",
                "Scheduling alarm for booking ID ${book.id} (Notif ID $uniqueNotificationID) at timestamp: $triggerTimeMillis"
            )
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent
                )
                Log.i("TAG###", "Alarm scheduled successfully for booking ID ${book.id}.")
            } catch (se: SecurityException) {
                Log.e(
                    "TAG###", "SecurityException for booking ID ${book.id}. Check permissions.", se
                )
            } catch (e: Exception) {
                Log.e("TAG###", "Exception setting alarm for booking ID ${book.id}", e)
            }

        } else {
            Log.w(
                "TAG###",
                "Not scheduling alarm for booking ID ${book.id}. Invalid time ($triggerTimeMillis) or time is in the past."
            )
        }
    }


    fun convertDateFormat(inputDate: String): String? {
        return try {
            val fromFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val toFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = fromFormat.parse(inputDate)
            date?.let { toFormat.format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun getTime(book: Booking): Long {

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        Log.i("TAG###", "book.date----: ${book.date}")
        Log.i("TAG###", "getTime----: ${convertDateFormat(book.date)}")

        val date = dateFormat.parse(convertDateFormat(book.date))
        val time = timeFormat.parse(book.time)

        val calendar = Calendar.getInstance()

        date?.let {
            val tempCalendar = Calendar.getInstance()
            tempCalendar.time = it
            val day = tempCalendar.get(Calendar.DAY_OF_MONTH)
            val month = tempCalendar.get(Calendar.MONTH)
            val year = tempCalendar.get(Calendar.YEAR)

            time?.let {
                val tempTimeCalendar = Calendar.getInstance()
                tempTimeCalendar.time = it
                val hour = tempTimeCalendar.get(Calendar.HOUR_OF_DAY)
                val minute = tempTimeCalendar.get(Calendar.MINUTE)

                calendar.set(year, month, day, hour, minute)
            }
        }
        return calendar.timeInMillis
    }


    fun delete(booking: Booking) = viewModelScope.launch {
        if (booking.id != 0) { // Only try to cancel if ID is valid
            cancelNotification(booking)
        } else {
            Log.w("TAG###", "Attempting to delete booking with invalid ID 0.")
        }
        repository.delete(booking)
        Log.d("TAG###", "Booking deleted with ID: ${booking.id}")
    }

    fun cancelNotification(book: Booking) {
        if (book.id == 0) {
            Log.w("TAG###", "Cannot cancel notification for booking with invalid ID 0.")
            return
        }
        val appContext =
            BookingApplication.mInstance/*getApplication<Application>().applicationContext*/
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, Notification::class.java) // Match the scheduling intent
        val uniqueNotificationID: Int = book.id //.toInt() // Use the same ID logic as scheduling

        // Recreate the PendingIntent EXACTLY as when scheduling, but use FLAG_NO_CREATE
        // to check if it exists without creating a new one.
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            uniqueNotificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE // Check existence
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel() // Cancel the PendingIntent itself
            Log.i(
                "TAG###",
                "Cancelled alarm for booking ID ${book.id} (Notif ID $uniqueNotificationID)."
            )
        } else {
            Log.w(
                "TAG###",
                "PendingIntent for booking ID ${book.id} (Notif ID $uniqueNotificationID) not found. Cannot cancel (already fired or never set?)."
            )
        }
    }


}
