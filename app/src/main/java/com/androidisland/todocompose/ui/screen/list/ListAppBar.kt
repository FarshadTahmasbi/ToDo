package com.androidisland.todocompose.ui.screen.list

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R
import com.androidisland.todocompose.component.PriorityItem
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.ui.theme.alphaDisabled
import com.androidisland.todocompose.ui.theme.dimens
import com.androidisland.todocompose.util.SearchAppBarState


@Composable
fun ListAppBar(
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    var searchAppBarState by rememberSaveable {
        mutableStateOf(SearchAppBarState.CLOSED)
    }

    var searchQueryState by rememberSaveable {
        mutableStateOf("")
    }

    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    searchAppBarState = SearchAppBarState.OPENED
                }, onSortClicked = onSortClicked,
                onDeleteClicked = onDeleteClicked
            )
        }

        else -> {
            SearchAppBar(
                text = searchQueryState,
                onTextChanged = {
                    searchQueryState = it
                },
                onCloseClicked = {
                    if (searchQueryState.isNotEmpty()) searchQueryState = ""
                    else searchAppBarState = SearchAppBarState.CLOSED
                },
                onSearchClicked = {
                    onSearchClicked(searchQueryState)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit, onSortClicked: (Priority) -> Unit, onDeleteClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.list_app_bar_title),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ), actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteClicked = onDeleteClicked
            )
        })
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit, onSortClicked: (Priority) -> Unit, onDeleteClicked: () -> Unit
) {
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteClicked = onDeleteClicked)
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
    onSortClicked: (Priority) -> Unit
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
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(Priority.LOW)
            }, text = { PriorityItem(priority = Priority.LOW) })
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(Priority.MEDIUM)
            }, text = { PriorityItem(priority = Priority.MEDIUM) })
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(Priority.HIGH)
            }, text = { PriorityItem(priority = Priority.HIGH) })
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(Priority.NONE)
            }, text = { PriorityItem(priority = Priority.NONE) })
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit
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
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteClicked()
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
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(MaterialTheme.dimens.topAppBarHeight),
        tonalElevation = MaterialTheme.dimens.topAppBarElevation,
        color = MaterialTheme.colorScheme.primary
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = onTextChanged,
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
                IconButton(onClick = onCloseClicked) {
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
    }
}

@Preview
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(onSearchClicked = {}, onSortClicked = {}, onDeleteClicked = {})
}

@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(text = "Keyword", onTextChanged = {}, onCloseClicked = {}, onSearchClicked = {})
}