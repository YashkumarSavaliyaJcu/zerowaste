package com.yash026.zerowaste.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.yash026.zerowaste.composables.CenteredLoadingIndicator
import com.yash026.zerowaste.listitem.MealItem
import com.yash026.zerowaste.viewmodels.SearchMealViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController, viewModel: SearchMealViewModel = viewModel()
) {

    var categoryToFetch by remember { mutableStateOf("") } // State for category

    LaunchedEffect(key1 = categoryToFetch) {
        viewModel.fetchMeals(categoryToFetch)
    }


    val meals by viewModel.meals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()


    Scaffold(

        topBar = {
            TopAppBar(title = { Text("Recipe List") })
        }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 65.dp)
        ) {

            if (isLoading) {
                CenteredLoadingIndicator()
            } else if (error != null) {
                Text("Error: $error")
            } else {

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),

                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(meals, key = { it.idMeal }) { meal ->

                        MealItem(
                            meal = meal,
                            onItemClick = { mealId ->

                                navController.navigate("detail/$mealId")
                            },
                        )
                    }
                }
            }
        }
    }


}