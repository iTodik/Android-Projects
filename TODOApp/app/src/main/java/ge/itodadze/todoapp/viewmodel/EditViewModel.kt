package ge.itodadze.todoapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.todoapp.data.database.ToDoDatabase
import ge.itodadze.todoapp.domain.repository.ItemsRepository
import ge.itodadze.todoapp.domain.repository.ToDoRepository
import ge.itodadze.todoapp.domain.repository.implementation.ItemsRepositoryImpl
import ge.itodadze.todoapp.domain.repository.implementation.ToDoRepositoryImpl
import ge.itodadze.todoapp.presenter.service.EditService
import ge.itodadze.todoapp.viewmodel.models.ViewItem
import kotlinx.coroutines.*

class EditViewModel(toDoRepository: ToDoRepository,
                    itemsRepository: ItemsRepository) : ViewModel() {

    private val service: EditService = EditService(toDoRepository, itemsRepository)
    private var initialId: Int? = null

    private var inMemoryTitle: String? = null
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>get() = _title

    private val _pin = MutableLiveData<Boolean>()
    val pin: LiveData<Boolean>get() = _pin

    private var inMemoryUnchecked: List<ViewItem> = ArrayList()
    private val _unchecked = MutableLiveData<List<ViewItem>>()
    val unchecked: LiveData<List<ViewItem>>get() = _unchecked

    private val _checked = MutableLiveData<List<ViewItem>>()
    val checked: LiveData<List<ViewItem>>get() = _checked

    private val _newActive = MutableLiveData<Int>()
    val newActive: LiveData<Int>get() = _newActive

    init {
        viewModelScope.launch {
            initNew()
        }
    }

    fun todoId(id: Int) {
        viewModelScope.launch {
            if (id != 0) {
                get(id)
            } else {
                initNew()
            }
            _title.value = inMemoryTitle
        }
    }

    fun changeTitle(text: String) {
        viewModelScope.launch {
            inMemoryTitle = text
        }
    }

    fun saveData(){
        viewModelScope.launch {
            service.update(initialId, inMemoryTitle!!, _pin.value!!, inMemoryUnchecked, _checked.value!!)
        }
    }

    fun pinUpdate() {
        viewModelScope.launch {
            val curr: Boolean = _pin.value!!
            _pin.value = !curr
        }
    }

    fun addItem() {
        viewModelScope.launch {
            inMemoryUnchecked = inMemoryUnchecked + ViewItem("", false)
            _unchecked.value = inMemoryUnchecked.toList()
            _newActive.value = inMemoryUnchecked.size - 1
        }
    }

    fun itemSaved() {
        viewModelScope.launch {
            _unchecked.value = inMemoryUnchecked.toList()
        }
    }

    fun checkAtPosition(position: Int) {
        viewModelScope.launch {
            if (inMemoryUnchecked.size > position) {
                val item: ViewItem = inMemoryUnchecked[position]

                inMemoryUnchecked = inMemoryUnchecked.subList(0, position) +
                        inMemoryUnchecked.subList(position + 1, inMemoryUnchecked.size)
                _unchecked.value = inMemoryUnchecked.toList()

                _checked.value = _checked.value!! + ViewItem(item.text, true)

            }
        }
    }

    fun deleteAtPosition(position: Int) {
        viewModelScope.launch {
            if (inMemoryUnchecked.size > position) {

                inMemoryUnchecked = inMemoryUnchecked.subList(0, position) +
                        inMemoryUnchecked.subList(position + 1, inMemoryUnchecked.size)
                _unchecked.value = inMemoryUnchecked.toList()

            }
        }
    }

    fun updateTextAt(position: Int, text: String) {
        viewModelScope.launch {
            if (inMemoryUnchecked.size > position) {

                inMemoryUnchecked = inMemoryUnchecked.subList(0, position) +
                        ViewItem(text, false) +
                        inMemoryUnchecked.subList(position + 1, inMemoryUnchecked.size)

            }
        }
    }

    private fun initNew() {
        inMemoryTitle = ""
        _pin.value = false
        inMemoryUnchecked = ArrayList()
        _unchecked.value = inMemoryUnchecked.toList()
        _checked.value = ArrayList()
    }

    private suspend fun get(id: Int) {
        initialId = id
        inMemoryTitle = service.getName(id)
        _pin.value = service.getPin(id)
        inMemoryUnchecked = service.getUnchecked(id)
        _unchecked.value = inMemoryUnchecked.toList()
        _checked.value = service.getChecked(id)
    }

    companion object {
        fun getEditViewModelFactory(context: Context): EditViewModelFactory {
            return EditViewModelFactory(context)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class EditViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditViewModel(
            ToDoRepositoryImpl(ToDoDatabase.getInstance(context).toDoDAO()),
            ItemsRepositoryImpl(ToDoDatabase.getInstance(context).itemsDAO())
        ) as T
    }
}