package ge.itodadze.todoapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.databinding.TodoListBinding
import ge.itodadze.todoapp.presenter.model.ToDoWithList
import ge.itodadze.todoapp.view.listener.ListItemClickListener

class MainItemsAdapter(private var todos: List<ToDoWithList>, private val listener: ListItemClickListener):
    RecyclerView.Adapter<ListViewHolder>() {

    private lateinit var binding: TodoListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = TodoListBinding.inflate(LayoutInflater.from(parent.context))

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.name.text = todos[position].todo.name
        holder.todoItems.adapter = ToDoListAdapter(todos[position].items)
        holder.itemView.setOnClickListener {
            listener.listItemClicked(todos[position])
        }
    }

    fun update(newTodos: List<ToDoWithList>) {
        todos = newTodos
        notifyDataSetChanged()
    }

}