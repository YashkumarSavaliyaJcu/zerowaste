package com.yash026.zerowaste.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.yash026.zerowaste.R
import com.yash026.zerowaste.model.Booking
import com.yash026.zerowaste.utils.BookingApplication
import com.yash026.zerowaste.viewmodels.BookingViewModel
import com.yash026.zerowaste.viewmodels.BookingViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun HomeScreen(
    viewModel: BookingViewModel = viewModel(factory = BookingViewModelFactory((LocalContext.current.applicationContext as BookingApplication).repository))
) {

    val context = LocalContext.current
    // Top app bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Reminder") },
            )
        },

        ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 65.dp, start = 15.dp, end = 15.dp),

            ) {

            var title by remember { mutableStateOf("") }
            var date by remember { mutableStateOf("Select Date") }
            var time by remember { mutableStateOf("Select Time") }
            var description by remember { mutableStateOf("") }

            var imageUri by remember { mutableStateOf<Uri?>(null) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                imageUri = uri
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Centers the image inside the Box
            ) {

                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { launcher.launch("image/*") })
                } ?: Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "Andy Rubin",
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .size(100.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text("Title", fontSize = 20.sp, modifier = Modifier.padding(top = 10.dp, bottom = 3.dp))

            BasicTitleTextField(title) { title = it }

            Text("Date", fontSize = 20.sp, modifier = Modifier.padding(top = 10.dp, bottom = 3.dp))

            DatePickerField(date) { date = it }

            Text("Time", fontSize = 20.sp, modifier = Modifier.padding(top = 10.dp, bottom = 3.dp))

            TimePickerField(time) { time = it }

            Text(
                "Description",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 3.dp),
            )
            BasicDescriptionTextField(description) { description = it }

            Button(
                onClick = {

                    if (title.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && description.isNotEmpty() && imageUri != null) {
                        val booking = Booking(
                            id = 0,
                            date = date,
                            time = time,
                            image = copyUriToFile(context, imageUri!!).toString(),
                            title = title,
                            description = description
                        )
                        viewModel.insertAndSchedule(booking)

                        Toast.makeText(
                            BookingApplication.mInstance, "Rimder Add", Toast.LENGTH_SHORT
                        ).show()

                        Log.d(
                            "ReminderData",
                            "Title: $title, Date: $date, Time: $time, Description: $description, imageUri: $imageUri"
                        )

                        // Optional: Clear form after saving
                        title = ""
                        date = "Select Date"
                        time = "Select Time"
                        description = ""
                        imageUri = null


                    } else {
                        Toast.makeText(
                            BookingApplication.mInstance,
                            "All fields are required",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = true,
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(text = "Reminder")
            }

        }
    }

}
// Url code

fun copyUriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("selected_image", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun BasicTitleTextField(title: String, onTitleChange: (String) -> Unit) {


    Box(
        modifier = Modifier
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // Add border
            .fillMaxWidth()
            .padding(8.dp) // Add padding inside border
    ) {
        BasicTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text("Title", color = Color.Gray) // Placeholder text
                }
                innerTextField()
            })
    }
}

@Composable
fun BasicDescriptionTextField(title: String, onTitleChange: (String) -> Unit) {


    Box(
        modifier = Modifier
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // Add border
            .fillMaxWidth()
            .padding(8.dp) // Add padding inside border
    ) {
        BasicTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text("Description", color = Color.Gray) // Placeholder text
                }
                innerTextField()
            })
    }
}

@Composable
fun DatePickerField(date: String, onDateSelected: (String) -> Unit) {

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(selectedDate)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false }
            show()
        }
    }

    Box(
        modifier = Modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)) // Border
        .height(55.dp)
            .fillMaxWidth()
            .clickable { showDatePicker = true } // Click to show DatePicker
        .padding(start = 15.dp), contentAlignment = Alignment.CenterStart // Align text to start
    ) {
        Text(
            text = date, // Set selected date text
            fontSize = 18.sp, color = Color.Black
        )
    }
}

@Composable
fun TimePickerField(time: String, onDateSelected: (String) -> Unit) {

    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showTimePicker) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                onDateSelected(selectedTime)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24-hour format
        ).apply {
            setOnDismissListener { showTimePicker = false }
            show()
        }
    }

    Box(
        modifier = Modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)) // Border
        .height(55.dp)
            .fillMaxWidth()
            .clickable { showTimePicker = true } // Opens Time Picker
        .padding(start = 15.dp), contentAlignment = Alignment.CenterStart) {
        Text(
            text = time, // Show selected time
            fontSize = 18.sp, color = Color.Black
        )
    }
}

