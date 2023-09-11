package com.androidisland.todocompose.ui.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.util.Constants.SPLASH_DISPLAY_DURATION
import com.androidisland.todocompose.util.Constants.SPLASH_LOGO_SIZE
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navigateToTaskList: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        delay(SPLASH_DISPLAY_DURATION)
        navigateToTaskList()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        SplashLogo(logoSize = SPLASH_LOGO_SIZE)
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(navigateToTaskList = {})
}

