package com.androidisland.todocompose.feature.tasklist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R
import com.androidisland.todocompose.common.theme.alphaDisabled
import com.androidisland.todocompose.common.theme.dimens
import com.androidisland.todocompose.component.DisplayAlertDialog
import com.androidisland.todocompose.component.PriorityItem
import com.androidisland.todocompose.enums.Priority
import com.androidisland.todocompose.enums.PrioritySort
import com.androidisland.todocompose.enums.SearchAppBarState


@Composable
fun TaskListAppBar(
    searchQuery: String?,
    sort: PrioritySort,
    onSortClicked: (PrioritySort) -> Unit,
    onDeleteAllClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    var searchQueryState by remember {
        val initialValue = searchQuery.orEmpty()
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(initialValue.length)
            )
        )
    }

    var searchAppBarState by rememberSaveable {
        mutableStateOf(
            if (searchQueryState.text.isEmpty()) SearchAppBarState.CLOSED
            else SearchAppBarState.OPENED
        )
    }

    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultTaskListAppBar(
                sort = sort,
                onSearchClicked = {
                    searchAppBarState = SearchAppBarState.OPENED
                }, onSortClicked = onSortClicked, onDeleteAllClicked = onDeleteAllClicked
            )
        }

        else -> {
            val focusManager = LocalFocusManager.current
            SearchAppBar(textFieldValue = searchQueryState, onTextChanged = {
                searchQueryState = it
            }, onCloseClicked = {
                if (searchQueryState.text.isNotEmpty()) searchQueryState = TextFieldValue(text = "")
                else {
                    searchAppBarState = SearchAppBarState.CLOSED
                    onCloseClicked()
                }
            }, onSearchClicked = {
                focusManager.clearFocus()
                onSearchClicked(searchQueryState.text)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTaskListAppBar(
    sort: PrioritySort,
    onSearchClicked: () -> Unit,
    onSortClicked: (PrioritySort) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.list_app_bar_title),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ), actions = {
            TaskListAppBarActions(
                sort = sort,
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllClicked = onDeleteAllClicked
            )
        })
}

@Composable
fun TaskListAppBarActions(
    sort: PrioritySort,
    onSearchClicked: () -> Unit,
    onSortClicked: (PrioritySort) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    var isDialogVisible by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_task_confirm_title),
        message = AnnotatedString(stringResource(id = R.string.delete_all_task_confirm_msg)),
        isDialogVisible = isDialogVisible,
        closeDialog = { isDialogVisible = false }) {
        onDeleteAllClicked()
    }

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(sort = sort, onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllClicked = { isDialogVisible = true })
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_action),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SortAction(
    sort: PrioritySort,
    onSortClicked: (PrioritySort) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.search_action),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(PrioritySort.LOW_TO_HIGH)
                },
                text = {
                    PriorityItem(
                        priority = Priority.LOW,
                        isSelected = sort == PrioritySort.LOW_TO_HIGH
                    )
                })
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(PrioritySort.HIGH_TO_LOW)
                },
                text = {
                    PriorityItem(
                        priority = Priority.HIGH,
                        isSelected = sort == PrioritySort.HIGH_TO_LOW
                    )
                })
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(PrioritySort.DEFAULT)
                },
                text = {
                    PriorityItem(
                        priority = Priority.NONE,
                        isSelected = sort == PrioritySort.DEFAULT
                    )
                })

        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(expanded = expanded,
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteAllClicked()
            }, text = {
                Text(
                    modifier = Modifier.padding(start = MaterialTheme.dimens.largePadding),
                    text = stringResource(id = R.string.delete_all_action)
                )
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(MaterialTheme.dimens.topAppBarHeight),
        shadowElevation = MaterialTheme.dimens.elevationMedium,
        color = MaterialTheme.colorScheme.primary
    ) {
        val focusRequester = remember { FocusRequester() }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = textFieldValue,
            onValueChange = { onTextChanged(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(alphaDisabled),
                    text = stringResource(id = R.string.list_app_search_placeholder),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = onSearchClicked) {
                    Icon(
                        modifier = Modifier.alpha(alphaDisabled),
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = onCloseClicked
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked()
            }),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = Color.Transparent
            )
        )

        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Preview
@Composable
private fun DefaultTaskListAppBarPreview() {
    DefaultTaskListAppBar(
        sort = PrioritySort.HIGH_TO_LOW,
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllClicked = {})
}

@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(textFieldValue = TextFieldValue(text = "Keyword"),
        onTextChanged = {},
        onCloseClicked = {},
        onSearchClicked = {})
}
