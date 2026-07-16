package com.treemiddle.photoexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.treemiddle.photoexplorer.feature.photolist.PhotoListScreen

@Composable
fun NavHost(controller: NavHostController = rememberNavController()) {
    NavHost(
        navController = controller,
        startDestination = Route.PHOTO_LIST
    ) {
        composable(route = Route.PHOTO_LIST) {
            PhotoListScreen()
        }
    }
}