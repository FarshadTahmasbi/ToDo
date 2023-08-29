package com.androidisland.todocompose.util


sealed class Either<out L, out R> {
    data class Left<L>(val data: L) : Either<L, Nothing>()
    data class Right<R>(val data: R) : Either<Nothing, R>()

    fun fold(isLeft: (data: L) -> Unit, isRight: (data: R) -> Unit) {
        when (this) {
            is Left -> isLeft(data)
            is Right -> isRight(data)
        }
    }
}