package ge.itodadze.todoapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class Item(
    @ColumnInfo(name="todo_id") val todoId: Int,
    val checked: Boolean,
    val text: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
