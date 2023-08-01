package ge.itodadze.todoapp.view.adapter

import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.databinding.TodoListBinding

class ListViewHolder(binding: TodoListBinding): RecyclerView.ViewHolder(binding.root) {
    val name = binding.todoNameList
    val todoItems = binding.todoListMain
}