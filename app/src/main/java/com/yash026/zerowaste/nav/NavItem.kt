package com.yash026.zerowaste.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import com.yash026.zerowaste.R

sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = R.drawable.round_home_24)

    object Items :
        Item(path = NavPath.PROFILE.toString(), title = NavTitle.RECIPE, icon = R.drawable.hamburger)

    object LIST :
        Item(path = NavPath.ITEMS.toString(), title = NavTitle.List, icon = R.drawable.round_grid_view_24)

}