package ge.itodadze.alarmapp.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.alarmapp.databinding.AlarmItemBinding
import ge.itodadze.alarmapp.view.listener.AlarmItemListener
import ge.itodadze.alarmapp.viewmodel.model.AlarmItem

class AlarmsListAdapter(private val alarmItemListener: AlarmItemListener,
                        private var list: List<AlarmItem>): RecyclerView.Adapter<AlarmItemViewHolder>() {
    private lateinit var binding: AlarmItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        binding = AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        holder.time.text = list[position].time
        holder.status.isChecked = list[position].active
        holder.status.setOnCheckedChangeListener {
                _, b -> alarmItemListener.alarmChecked(list[position], b)
        }
        holder.itemView.setOnLongClickListener {
            alarmItemListener.alarmLongClicked(list[position])
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<AlarmItem?>) {
        list = newList.filterNotNull()
        notifyDataSetChanged()
    }

}

class AlarmItemViewHolder(binding: AlarmItemBinding): RecyclerView.ViewHolder(binding.root) {
    var time = binding.alarmTime
    var status = binding.alarmStatus
}