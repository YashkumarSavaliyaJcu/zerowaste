package com.yash026.zerowaste.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yash026.zerowaste.activities.AllBookingsScreen
import com.yash026.zerowaste.activities.DetailScreen
import com.yash026.zerowaste.activities.HomeScreen
import com.yash026.zerowaste.activities.SearchScreen
import com.yash026.zerowaste.nav.NavItem

@Composable
fun NavigationScreens(navController: NavHostController) {
    NavHost(navController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen() }
        composable(NavItem.LIST.path) { AllBookingsScreen() }
        composable(NavItem.Items.path) { SearchScreen(navController) }
        composable("detail/{id}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("id")
            DetailScreen(recipeId ?: "Unknown", navController)
        }
    }
}