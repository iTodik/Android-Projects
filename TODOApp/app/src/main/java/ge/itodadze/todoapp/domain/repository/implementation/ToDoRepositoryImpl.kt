package ge.itodadze.todoapp.domain.repository.implementation

import ge.itodadze.todoapp.data.dao.ToDoDAO
import ge.itodadze.todoapp.data.entity.ToDo
import ge.itodadze.todoapp.domain.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToDoRepositoryImpl(private val dao: ToDoDAO): ToDoRepository {
    override suspend fun allWithPrefix(prefix: String): List<ToDo> {
        return withContext(Dispatchers.IO) { dao.getAllWithPrefix(prefix) }
    }

    override suspend fun byId(id: Int): ToDo {
        return withContext(Dispatchers.IO) { dao.get(id) }
    }

    override suspend fun add(toDo: ToDo): Long {
        return withContext(Dispatchers.IO) { dao.insert(toDo) }
    }

    override suspend fun remove(toDo: ToDo) {
        return withContext(Dispatchers.IO) { dao.delete(toDo) }
    }
}