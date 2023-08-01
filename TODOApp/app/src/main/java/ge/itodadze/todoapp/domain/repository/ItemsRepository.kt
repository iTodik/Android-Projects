package ge.itodadze.todoapp.domain.repository

import ge.itodadze.todoapp.data.entity.Item

interface ItemsRepository {
    suspend fun belongingTo(toDoId: Int): List<Item>
    suspend fun add(items: List<Item>)
    suspend fun removeBelongingTo(toDoId: Int)
}