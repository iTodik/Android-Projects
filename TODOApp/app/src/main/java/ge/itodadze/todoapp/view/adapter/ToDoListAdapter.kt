package ge.itodadze.todoapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.R
import ge.itodadze.todoapp.data.entity.Item
import ge.itodadze.todoapp.databinding.TodoFrozenItemBinding

class ToDoListAdapter(private var items: List<Item>): RecyclerView.Adapter<FrozenIndividualItemViewHolder>() {
    private lateinit var binding: TodoFrozenItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrozenIndividualItemViewHolder {
        binding = TodoFrozenItemBinding.inflate(LayoutInflater.from(parent.context))

        return FrozenIndividualItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (items.size > 3) {
            5
        } else {
            items.size
        }
    }

    override fun onBindViewHolder(holder: FrozenIndividualItemViewHolder, position: Int) {
        if (position < 3) {
            holder.box.isChecked = items[position].checked
            holder.text.text = items[position].text
        } else if(position == 3) {
            holder.box.visibility = View.GONE
            holder.text.text = binding.root.resources.getString(R.string.three_points)
            holder.text.setTextColor(holder.text.context.getColor(R.color.grey))
        } else {
            holder.box.visibility = View.GONE
            var checkedItems = 0
            for (item in items.subList(3, items.size)) {
                if (item.checked) checkedItems++
            }
            holder.text.text = binding.root.resources.getString(R.string.extra_items, checkedItems)
            holder.text.setTextColor(holder.text.context.getColor(R.color.grey))
        }
        holder.box.isEnabled = false
        holder.text.isEnabled = false
        holder.close.visibility = View.GONE
    }
}