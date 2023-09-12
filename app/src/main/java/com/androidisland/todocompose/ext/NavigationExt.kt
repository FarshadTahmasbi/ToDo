package com.androidisland.todocompose.ext

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.androidisland.todocompose.navigation.Screen


fun NavGraphBuilder.screenComposable(
    screen: Screen,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    val transitions = screen.transitions
    composable(
        route = screen.route,
        enterTransition = transitions.enterTransition,
        exitTransition = transitions.exitTransition,
        popEnterTransition = transitions.popEnterTransition,
        popExitTransition = transitions.popExitTransition,
        deepLinks = screen.deepLinks,
        content = content
    )
}