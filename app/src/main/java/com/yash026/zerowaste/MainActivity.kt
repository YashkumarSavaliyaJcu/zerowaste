package com.yash026.zerowaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash026.zerowaste.ui.theme.ZerowasteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZerowasteTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Home", "Food List", "Add Food", "Profile")
    val Image = listOf(
        Icons.Default.Home,
        Icons.Default.Home,
        Icons.Default.Add,
        Icons.Default.Person
    )

    val foodItems = listOf(
        FoodItemData(R.drawable.milk, "Milk", "Expires on: 25th March 2025", "Use for cooking before expiry."),
        FoodItemData(R.drawable.bread, "Bread", "Expires on: 22nd March 2025", "Store in a cool place."),
        FoodItemData(R.drawable.cheese, "Cheese", "Expires on: 30th March 2025", "Keep refrigerated."),
        FoodItemData(R.drawable.egg, "Eggs", "Expires on: 28th March 2025", "Consume within 2 weeks."),
        FoodItemData(R.drawable.yogurt, "Yogurt", "Expires on: 26th March 2025", "Keep chilled."),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Row {
                    Text("Zero Waste Tracker",)
                }

                     },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFbdffa2),
                    titleContentColor = Color.Black
                ),

            )
        },

        // add the bottom bar
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(Image[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foodItems) { food ->
                    FoodItem(
                        imageRes = food.imageRes,
                        title = food.title,
                        expiryDate = food.expiryDate,
                        note = food.note
                    )
                }
            }
        }
    }
}

@Composable
fun FoodItem(imageRes: Int, title: String, expiryDate: String, note: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        // row for centralize the dta
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Food Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            // data base
            Column {
                Text(text = title, fontSize = 20.sp, color = Color.Black)
                Text(text = expiryDate, fontSize = 14.sp, color = Color.Gray)
                Text(text = note, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}

data class FoodItemData(
    val imageRes: Int,
    val title: String,
    val expiryDate: String,
    val note: String
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZerowasteTheme {
        MainScreen()
    }
}
