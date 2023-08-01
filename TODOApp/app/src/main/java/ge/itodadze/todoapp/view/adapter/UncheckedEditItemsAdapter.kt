package ge.itodadze.todoapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.todoapp.databinding.TodoFrozenItemBinding
import ge.itodadze.todoapp.databinding.TodoItemBinding
import ge.itodadze.todoapp.view.listener.EditItemListener
import ge.itodadze.todoapp.viewmodel.models.ViewItem

class UncheckedEditItemsAdapter(private var items: List<ViewItem>,
                                private val listener: EditItemListener,
                                private var active: Int?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EDITABLE) {
            val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context))

            EditableIndividualItemViewHolder(binding)
        } else {
            val binding = TodoFrozenItemBinding.inflate(LayoutInflater.from(parent.context))

            FrozenIndividualItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == EDITABLE) {
            val viewHolder: EditableIndividualItemViewHolder =
                holder as EditableIndividualItemViewHolder
            viewHolder.box.isChecked = false
            viewHolder.box.setOnClickListener {
                listener.itemChecked(position)
            }
            viewHolder.text.setText(items[position].text)
            viewHolder.close.visibility = View.VISIBLE
            viewHolder.close.setOnClickListener {
                listener.itemDeleteRequest(position)
            }
            viewHolder.text.setSelection(viewHolder.text.text.length)
            viewHolder.text.doOnTextChanged{
                text, _, _, _ -> listener.itemTextChanged(position, text.toString())
            }
        } else {
            val viewHolder: FrozenIndividualItemViewHolder =
                holder as FrozenIndividualItemViewHolder
            viewHolder.box.isChecked = false
            viewHolder.box.setOnClickListener {
                listener.itemChecked(position)
            }
            viewHolder.text.text = items[position].text
            viewHolder.text.setOnClickListener {
                listener.itemActivated(position)
            }
            viewHolder.close.visibility = View.GONE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == active) {
            EDITABLE
        } else {
            FROZEN
        }
    }

    fun update(newItems: List<ViewItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun update(newActive: Int?) {
        active = newActive
        listener.itemDeactivated()
        notifyDataSetChanged()
    }

    companion object {
        private const val FROZEN: Int = 0
        private const val EDITABLE: Int = 1
    }

}