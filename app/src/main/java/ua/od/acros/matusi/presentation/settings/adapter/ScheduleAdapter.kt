package ua.od.acros.matusi.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.checkedChanges
import ua.od.acros.matusi.domain.model.CareSchedule
import ua.od.acros.matusi.databinding.ScheduleItemBinding
import ua.od.acros.matusi.presentation.misc.getWeekDayNameLocalized

class ScheduleAdapter(
    private var onShowScheduleDialog: ((currentDay: CareSchedule) -> Unit)
): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {



    var schedules: List<CareSchedule> = listOf()
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScheduleItemBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        with(holder) {
            tag = position
            val schedule = schedules[tag]
            val txt = getWeekDayNameLocalized(schedule.day)
            with(btnDay) {
                text = txt
                textOff = txt
                textOn = txt
                isChecked = schedule.checked
            }
            val text = "${schedule.start} - ${schedule.end}"
            tvSchedule.text = text
        }
    }

    inner class ScheduleViewHolder(binding: ScheduleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val btnDay = binding.btnDay
        private val btnEdit = binding.btnEdit
        val tvSchedule = binding.tvSchedule
        var tag = -1

        init {
            btnDay.checkedChanges().subscribe { checked ->
                if (tag >= 0) {
                    schedules[tag].checked = checked
                }
                if (checked) {
                    btnEdit.isVisible = true
                } else {
                    btnEdit.isGone = true
                }
            }

            btnEdit.clicks().subscribe {
                onShowScheduleDialog(schedules[tag])
            }
        }
    }

    override fun getItemCount() = schedules.size
}