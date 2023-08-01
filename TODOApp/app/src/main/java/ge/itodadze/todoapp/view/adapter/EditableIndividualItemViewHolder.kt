package ge.itodadze.todoapp.view.adapter

import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.databinding.TodoItemBinding

class EditableIndividualItemViewHolder(binding: TodoItemBinding): RecyclerView.ViewHolder(binding.root) {
    val box: CheckBox = binding.checked
    val text: EditText = binding.itemName
    val close: ImageView = binding.closeButton
}