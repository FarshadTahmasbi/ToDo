package com.androidisland.todocompose.util


fun debounceClick(timeoutMillis: Long, onClick: () -> Unit): () -> Unit {
    var latest: Long = 0
    return {
        val now = System.currentTimeMillis()
        if (now - latest >= timeoutMillis) {
            latest = now
            onClick()
        }
    }
}