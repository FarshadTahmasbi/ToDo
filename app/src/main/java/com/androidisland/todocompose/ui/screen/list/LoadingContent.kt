package com.androidisland.todocompose.ui.screen.list

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidisland.todocompose.R
import com.androidisland.todocompose.ui.theme.dimens


@Composable
fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "Loading infinite animation")
        val degrees by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 360f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(durationMillis = 4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Loading rotation animation"
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Loading alpha animation"
        )
        Icon(
            modifier = Modifier
                .size(100.dp)
                .rotate(degrees)
                .alpha(alpha),
            painter = painterResource(id = R.drawable.ic_loading),
            contentDescription = stringResource(id = R.string.loading),
            tint = MaterialTheme.colorScheme.inverseOnSurface,
        )
        Text(
            modifier = Modifier
                .padding(MaterialTheme.dimens.largePadding)
                .alpha(alpha),
            text = stringResource(R.string.loading_msg),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun LoadingContentPreview() {
    LoadingContent()
}