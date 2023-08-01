package ge.itodadze.todoapp.presenter.model

import ge.itodadze.todoapp.data.entity.Item
import ge.itodadze.todoapp.data.entity.ToDo

data class ToDoWithList(val todo: ToDo, val items: List<Item>)