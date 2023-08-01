package ge.itodadze.todoapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.todoapp.data.database.ToDoDatabase
import ge.itodadze.todoapp.domain.repository.ItemsRepository
import ge.itodadze.todoapp.domain.repository.ToDoRepository
import ge.itodadze.todoapp.domain.repository.implementation.ItemsRepositoryImpl
import ge.itodadze.todoapp.domain.repository.implementation.ToDoRepositoryImpl
import ge.itodadze.todoapp.presenter.model.ToDoWithList
import ge.itodadze.todoapp.presenter.service.MainService
import kotlinx.coroutines.launch

class MainViewModel(
    toDoRepository: ToDoRepository,
    itemsRepository: ItemsRepository): ViewModel() {

    private val service: MainService = MainService(toDoRepository, itemsRepository)

    private val _pinned = MutableLiveData<List<ToDoWithList>>()
    val pinned: LiveData<List<ToDoWithList>>get() = _pinned

    private val _others = MutableLiveData<List<ToDoWithList>>()
    val others: LiveData<List<ToDoWithList>>get() = _others

    init { updateData("") }

    fun update(prefix: String) {
        updateData(prefix)
    }

    private fun updateData(prefix: String) {
        viewModelScope.launch {
            _pinned.value = service.getPinned(prefix)
            _others.value = service.getUnpinned(prefix)
        }
    }

    companion object {
        fun getMainViewModelFactory(context: Context): MainViewModelFactory {
            return MainViewModelFactory(context)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            ToDoRepositoryImpl(ToDoDatabase.getInstance(context).toDoDAO()),
            ItemsRepositoryImpl(ToDoDatabase.getInstance(context).itemsDAO())
        ) as T
    }
}