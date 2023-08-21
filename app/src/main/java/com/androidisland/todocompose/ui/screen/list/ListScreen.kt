package com.androidisland.todocompose.ui.screen.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navigateToTaskScreen: (Int) -> Unit) {
    Scaffold(
        content = { it.toString() },
        floatingActionButton = { ListFab(navigateToTaskScreen) }
    )
}

@Composable
fun ListFab(
    onFabClicked: (Int) -> Unit
) {
    FloatingActionButton(onClick = { onFabClicked(-1) }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(
                id = R.string.add_button
            ),
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun ListScreenPreview() {
    ListScreen(navigateToTaskScreen = {})
}
