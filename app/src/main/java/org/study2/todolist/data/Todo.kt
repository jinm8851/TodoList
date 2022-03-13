package org.study2.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Todo(
    var title: String,
    var date: Long = Calendar.getInstance().timeInMillis,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}