package com.androidisland.todocompose.data.models

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidisland.todocompose.ext.getParcelableCompat
import com.androidisland.todocompose.util.Constants.DATABASE_TABLE
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = DATABASE_TABLE)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
) : Parcelable

class ToDoTaskNavType : NavType<ToDoTask>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): ToDoTask? =
        bundle.getParcelableCompat(key)

    override fun parseValue(value: String): ToDoTask =
        Gson().fromJson(value, ToDoTask::class.java)


    override fun put(bundle: Bundle, key: String, value: ToDoTask) {
        bundle.putParcelable(key, value)
    }

}