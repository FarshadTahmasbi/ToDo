package com.androidisland.todocompose.ui.screen.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidisland.todocompose.util.Constants.SPLASH_DISPLAY_DURATION
import com.androidisland.todocompose.util.Constants.SPLASH_LOGO_SIZE
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navigateToTaskList: () -> Unit) {

    var startAnimation by remember {
        mutableStateOf(false)
    }

    val offsetState by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 1000),
        label = "Splash logo offset animation"
    )

    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Splash logo alpha animation"
    )

    LaunchedEffect(key1 = Unit) {
        startAnimation = true
        delay(SPLASH_DISPLAY_DURATION)
        navigateToTaskList()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        SplashLogo(
            logoSize = SPLASH_LOGO_SIZE,
            offsetY = offsetState,
            alpha = alphaState
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(navigateToTaskList = {})
}

