package com.yash026.zerowaste.activities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash026.zerowaste.listitem.FoodItem
import com.yash026.zerowaste.utils.ZeroWasteApplication
import com.yash026.zerowaste.viewmodels.ListItemViewModel
import com.yash026.zerowaste.viewmodels.ListItemViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: ListItemViewModel = viewModel(factory = ListItemViewModelFactory((LocalContext.current.applicationContext as ZeroWasteApplication).repository))
) {

    val bookings by viewModel.allBookings.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "All Remindar",
                        color = Color.Black // Optional: change text color
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFA8FF97) // 🍊 Orange background
                )
            )

        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 65.dp, start = 15.dp, end = 15.dp)
        ) {


            if (bookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data found", textAlign = TextAlign.Center)
                }

            } else {
                Column() {

                    LazyColumn {
                        items(bookings) { booking ->
                            FoodItem(booking, viewModel)
                        }
                    }

                }
            }
        }
    }


}
