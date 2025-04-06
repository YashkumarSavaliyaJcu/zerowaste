package com.yash026.zerowaste.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart

sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Icons.Default.Home)

    object Items :
        Item(path = NavPath.PROFILE.toString(), title = NavTitle.RECIPE, icon = Icons.Default.ShoppingCart)

    object LIST :
        Item(path = NavPath.ITEMS.toString(), title = NavTitle.List, icon = Icons.Default.List)

}