package ge.itodadze.todoapp.presenter.service

import ge.itodadze.todoapp.data.entity.ToDo
import ge.itodadze.todoapp.domain.repository.ItemsRepository
import ge.itodadze.todoapp.domain.repository.ToDoRepository
import ge.itodadze.todoapp.presenter.model.ToDoWithList

class MainService(private val toDoRepo: ToDoRepository,
                  private val itemsRepo: ItemsRepository) {

    suspend fun getPinned(prefix: String): List<ToDoWithList> {
        return getBasedOnPin(prefix) { it.isPinned }
    }

    suspend fun getUnpinned(prefix: String): List<ToDoWithList> {
        return getBasedOnPin(prefix) { !it.isPinned }
    }

    private suspend fun getBasedOnPin(prefix: String, predicate: (ToDo) -> Boolean): List<ToDoWithList> {
        return toDoRepo.allWithPrefix(prefix).filter{predicate(it)}
            .map{ ToDoWithList(it, itemsRepo.belongingTo(it.id)) }
    }

}