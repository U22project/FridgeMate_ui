package com.example.fridgemate.components

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import com.example.fridgemate.R

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val iconVector: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null
) {
    object Home      : BottomNavItem("home", "ホーム", Icons.Default.Home)
    object Search    : BottomNavItem("search", "検索", Icons.Default.Search)
    object AddFridge : BottomNavItem("CameraScreen", "追加", Icons.Default.AddCircle)
    object Config    : BottomNavItem("shopping", "メモ", Icons.Default.Menu)

    // 冷蔵庫アイコンは Vector Asset で ic_fridge.xml を追加して使う
    object InvScreen : BottomNavItem("inventory", "冷蔵庫", iconRes = R.drawable.fridge_icon)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Search,
    BottomNavItem.AddFridge,
    BottomNavItem.InvScreen,
    BottomNavItem.Config
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    when {
                        item.iconVector != null ->
                            Icon(imageVector = item.iconVector, contentDescription = item.label)
                        item.iconRes != null ->
                            Icon(painter = painterResource(id = item.iconRes), contentDescription = item.label)
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}
