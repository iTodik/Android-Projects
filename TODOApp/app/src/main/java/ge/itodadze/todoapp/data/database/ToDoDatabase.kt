package ge.itodadze.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ge.itodadze.todoapp.data.dao.ItemsDAO
import ge.itodadze.todoapp.data.dao.ToDoDAO
import ge.itodadze.todoapp.data.entity.Item
import ge.itodadze.todoapp.data.entity.ToDo

@Database(entities = [ToDo::class, Item::class], version = 11, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDAO(): ToDoDAO
    abstract fun itemsDAO(): ItemsDAO

    companion object {
        @Volatile
        private var instance: ToDoDatabase? = null

        fun getInstance(context: Context): ToDoDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(context, ToDoDatabase::class.java, "db_todo")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance!!
        }
    }
}