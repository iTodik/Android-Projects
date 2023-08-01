package ge.itodadze.todoapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todo")
data class ToDo(
    val name: String,
    @ColumnInfo(name="is_pinned") val isPinned: Boolean
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    @ColumnInfo(name="created_at") var createdAt: String = Calendar.getInstance().time.toString()
}