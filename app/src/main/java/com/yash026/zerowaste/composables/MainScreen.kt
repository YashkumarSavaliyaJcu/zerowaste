package com.yash026.zerowaste.composables

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoutePattern = currentBackStackEntry?.destination?.route

    val routesToHideBottomBar = setOf(
        "detail/{id}",
        "mealScreen/{category}",
        "mealDetailScreen/{mealId}",
        "search"
    )
    Scaffold(
        bottomBar = {
            if (currentRoutePattern !in routesToHideBottomBar) {
                BottomAppBar {
                    BottomNavigationBar(navController = navController)
                }
            }
        }
    ) { innerPadding ->

//        Column(Modifier.padding(innerPadding)) {

        NavigationScreens(navController = navController)
//        }

    }
}

