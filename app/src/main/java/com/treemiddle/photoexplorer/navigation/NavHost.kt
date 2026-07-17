package com.treemiddle.photoexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.treemiddle.photoexplorer.feature.likedphotolist.LikedPhotoListScreen
import com.treemiddle.photoexplorer.feature.photodetail.PhotoDetailScreen
import com.treemiddle.photoexplorer.feature.photolist.PhotoListScreen

@Composable
fun NavHost(controller: NavHostController = rememberNavController()) {
    NavHost(
        navController = controller,
        startDestination = Route.PHOTO_LIST
    ) {
        composable(route = Route.PHOTO_LIST) {
            PhotoListScreen(
                onNavigateToDetail = { photoId ->
                    controller.navigate(route = Route.detail(photoId = photoId))
                },
                onNavigateToLiked = {
                    controller.navigate(route = Route.LIKED_PHOTO_LIST)
                }
            )
        }
        composable(
            route = Route.DETAIL,
            arguments = listOf(navArgument(name = Route.PHOTO_ID) {
                type = NavType.StringType
            })
        ) {
            PhotoDetailScreen()
        }
        composable(route = Route.LIKED_PHOTO_LIST) {
            LikedPhotoListScreen()
        }
    }
}