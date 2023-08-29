package com.androidisland.todocompose.util


enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION;

    fun isEditMode(): Boolean {
        return this in setOf(ADD, UPDATE)
    }

    companion object {
        fun from(name: String?) = values().firstOrNull { it.name == name?.uppercase() }
    }
}