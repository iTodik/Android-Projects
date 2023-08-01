package ge.itodadze.todoapp.data.dao

import androidx.room.*
import ge.itodadze.todoapp.data.entity.Item

@Dao
interface ItemsDAO {

    @Query("SELECT * FROM todo_items WHERE todo_id=:toDoId")
    fun getItemsByToDoId(toDoId: Int): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<Item>)

    @Delete(entity = Item::class)
    fun deleteByName(vararg todoId: TodoName)

}

data class TodoName(val todo_id: Int)
