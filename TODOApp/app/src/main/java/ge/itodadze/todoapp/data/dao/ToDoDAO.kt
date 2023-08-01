package ge.itodadze.todoapp.data.dao

import androidx.room.*
import ge.itodadze.todoapp.data.entity.ToDo

@Dao
interface ToDoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(toDo: ToDo): Long

    @Query("SELECT * FROM todo WHERE id=:identifier")
    fun get(identifier: Int): ToDo

    @Query("SELECT * FROM todo WHERE name LIKE :prefix || '%' ORDER BY created_at DESC")
    fun getAllWithPrefix(prefix: String): List<ToDo>

    @Delete
    fun delete(toDo: ToDo)

}