package com.androidisland.todocompose.common.resource


interface ResProvider<I, O> {
    operator fun get(input: I): O
}