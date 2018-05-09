package workshop.akbolatss.tagsnews.screen.reminders


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rv_reminder_item.view.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.model.dao.ReminderItem
import java.util.*
import java.util.regex.Pattern

class RemindersAdapter(private val mListener: ReminderListener) : RecyclerView.Adapter<RemindersAdapter.RemindersVH>() {

    private val mList: MutableList<ReminderItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersVH {
        val mLayoutInflater = LayoutInflater.from(parent.context)
        val view = mLayoutInflater.inflate(R.layout.rv_reminder_item, parent, false)
        return RemindersVH(view)
    }

    override fun onBindViewHolder(holder: RemindersVH, position: Int) {
        val rItem = mList[position]
        holder.bind(rItem, mListener)
    }

    fun onAddItems(rItems: List<ReminderItem>?) {
        if (rItems != null) {
            mList.clear()
            mList.addAll(rItems)
            notifyDataSetChanged()
        }
    }

    fun onAddItem(rItem: ReminderItem?) {
        if (rItem != null) {
            mList.add(rItem)
            notifyItemInserted(mList.size - 1)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateItem(rItem: ReminderItem, pos: Int) {
        if (rItem != null) {
            mList[pos] = rItem
            notifyItemChanged(pos)
        }
    }

    fun removeItem(rItem: ReminderItem, pos: Int) {
        if (rItem != null) {
            mList.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    interface ReminderListener {
        fun onReminderSwitch(rItem: ReminderItem, isActive: Boolean, pos: Int)

        fun onReminderOptions(rItem: ReminderItem, view: View, pos: Int)
    }

    inner class RemindersVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rItem: ReminderItem, mListener: ReminderListener) {
            itemView.tvTime.text = onTimeFixer(rItem.hour) + ":" + onTimeFixer(rItem.minute)
            iconState(rItem.isActive)

            itemView.imgOnOff.setOnClickListener {
                mListener.onReminderSwitch(rItem, !rItem.isActive, adapterPosition)
                iconState(rItem.isActive)
            }
            itemView.imgOptions.setOnClickListener {
                mListener.onReminderOptions(rItem, it, adapterPosition)
            }
        }

        private fun iconState(isActive: Boolean) {
            if (isActive) {
                itemView.imgOnOff.setImageResource(R.drawable.ic_timer_24dp)
            } else {
                itemView.imgOnOff.setImageResource(R.drawable.ic_timer_off_24dp)
            }
        }

        private fun onTimeFixer(time: Int): String {
            if (time == 0) {
                return "00"
            }
            val ptr = Pattern.compile("^(?:[1-9]|0[1-9])$")
            return if (ptr.matcher(time.toString()).matches()) {
                "0$time"
            } else time.toString()
        }
    }
}
