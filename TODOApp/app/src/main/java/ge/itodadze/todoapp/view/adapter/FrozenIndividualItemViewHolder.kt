package ge.itodadze.todoapp.view.adapter

import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.databinding.TodoFrozenItemBinding

class FrozenIndividualItemViewHolder(binding: TodoFrozenItemBinding): RecyclerView.ViewHolder(binding.root) {
    val box: CheckBox = binding.checked
    val text: TextView = binding.itemName
    val close: ImageView = binding.closeButton
}
