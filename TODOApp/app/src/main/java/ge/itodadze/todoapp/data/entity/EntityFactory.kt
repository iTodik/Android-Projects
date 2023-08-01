package ge.itodadze.todoapp.data.entity

class EntityFactory {
    companion object {
        fun defaultToDoWithId(id: Int): ToDo {
            val todo = ToDo("", false)
            todo.id = id
            return todo
        }

        fun toDoWithId(title: String, isPinned: Boolean, id: Int?): ToDo {
            val todo = ToDo(title, isPinned)
            if (id != null) todo.id = id
            return todo
        }
    }
}