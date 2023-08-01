package ge.itodadze.todoapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.R
import ge.itodadze.todoapp.databinding.TodoFrozenItemBinding
import ge.itodadze.todoapp.viewmodel.models.ViewItem

class CheckedEditItemsAdapter(private var items: List<ViewItem>): RecyclerView.Adapter<FrozenIndividualItemViewHolder>() {
    private lateinit var binding: TodoFrozenItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrozenIndividualItemViewHolder {
        binding = TodoFrozenItemBinding.inflate(LayoutInflater.from(parent.context))

        return FrozenIndividualItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FrozenIndividualItemViewHolder, position: Int) {
        holder.box.isChecked = true
        holder.box.isEnabled = false
        holder.text.text = items[position].text
        holder.text.setTextColor(holder.text.context.getColor(R.color.grey))
        holder.close.visibility = View.GONE
    }

    fun update(newItems: List<ViewItem>) {
        items = newItems
        notifyDataSetChanged()
    }

}