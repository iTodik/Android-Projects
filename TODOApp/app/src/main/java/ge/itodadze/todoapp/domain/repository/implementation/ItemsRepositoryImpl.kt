package ge.itodadze.todoapp.domain.repository.implementation

import ge.itodadze.todoapp.data.dao.ItemsDAO
import ge.itodadze.todoapp.data.dao.TodoName
import ge.itodadze.todoapp.data.entity.Item
import ge.itodadze.todoapp.domain.repository.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemsRepositoryImpl(private val dao: ItemsDAO) : ItemsRepository {
    override suspend fun belongingTo(toDoId: Int): List<Item> {
        return withContext(Dispatchers.IO) { dao.getItemsByToDoId(toDoId) }
    }

    override suspend fun add(items: List<Item>) {
        return withContext(Dispatchers.IO) { dao.insert(items) }
    }

    override suspend fun removeBelongingTo(toDoId: Int) {
        return withContext(Dispatchers.IO) { dao.deleteByName(TodoName(toDoId)) }
    }
}