package ge.itodadze.todoapp.view.listener

import ge.itodadze.todoapp.presenter.model.ToDoWithList

interface ListItemClickListener {
    fun listItemClicked(toDoItem: ToDoWithList)
}