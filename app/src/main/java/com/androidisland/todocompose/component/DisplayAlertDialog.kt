package com.androidisland.todocompose.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R


@Composable
fun DisplayAlertDialog(
    title: String,
    message: AnnotatedString,
    isDialogVisible: Boolean,
    closeDialog: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (isDialogVisible) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onDismissRequest = closeDialog,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    closeDialog()
                }) {
                    Text(
                        text = stringResource(R.string.yes),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    closeDialog()
                }) {
                    Text(
                        text = stringResource(R.string.no),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            })
    }
}

@Preview
@Composable
fun DisplayAlertDialogPreview() {
    DisplayAlertDialog(
        title = "Delete",
        message = buildAnnotatedString { append("Are you sure?") },
        isDialogVisible = true,
        closeDialog = {},
        onConfirm = {})
}