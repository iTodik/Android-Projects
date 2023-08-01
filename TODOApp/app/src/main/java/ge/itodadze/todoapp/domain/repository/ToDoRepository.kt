package ge.itodadze.todoapp.domain.repository

import ge.itodadze.todoapp.data.entity.ToDo

interface ToDoRepository {
    suspend fun allWithPrefix(prefix: String): List<ToDo>
    suspend fun byId(id: Int): ToDo
    suspend fun add(toDo: ToDo): Long
    suspend fun remove(toDo: ToDo)
}