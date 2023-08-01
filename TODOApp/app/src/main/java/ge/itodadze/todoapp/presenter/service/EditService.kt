package ge.itodadze.todoapp.presenter.service

import ge.itodadze.todoapp.data.entity.EntityFactory
import ge.itodadze.todoapp.data.entity.Item
import ge.itodadze.todoapp.domain.repository.ItemsRepository
import ge.itodadze.todoapp.domain.repository.ToDoRepository
import ge.itodadze.todoapp.viewmodel.models.ViewItem

class EditService(private val toDoRepo: ToDoRepository,
                  private val itemsRepo: ItemsRepository) {

    suspend fun getUnchecked(id: Int): List<ViewItem> {
        return itemsRepo.belongingTo(id).filter{!it.checked}.map{ViewItem.getFromItem(it) }
    }

    suspend fun getChecked(id: Int): List<ViewItem> {
        return itemsRepo.belongingTo(id).filter{it.checked}.map{ViewItem.getFromItem(it)}
    }

    suspend fun getPin(id: Int): Boolean {
        return toDoRepo.byId(id).isPinned
    }

    suspend fun getName(id: Int): String {
        return toDoRepo.byId(id).name
    }

    suspend fun update(id: Int?, title: String, isPinned: Boolean,
                       unchecked: List<ViewItem>, checked: List<ViewItem>) {
        if (id != null) {
            toDoRepo.remove(EntityFactory.defaultToDoWithId(id))
            itemsRepo.removeBelongingTo(id)
        }
        val identifier = toDoRepo.add(EntityFactory.toDoWithId(title, isPinned, id))
        itemsRepo.add((unchecked + checked).map{Item(identifier.toInt(), it.checked, it.text)})
    }

}