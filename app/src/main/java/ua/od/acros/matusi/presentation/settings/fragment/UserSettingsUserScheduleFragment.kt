package ua.od.acros.matusi.presentation.settings.fragment

import android.text.format.DateFormat.is24HourFormat
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.domain.model.CareSchedule
import ua.od.acros.matusi.databinding.FragmentUserSettingsUserScheduleBinding
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel
import ua.od.acros.matusi.presentation.settings.adapter.ScheduleAdapter
import java.time.DayOfWeek

class UserSettingsUserScheduleFragment (private val sharedViewModel: UserSettingsViewModel):
    BaseSettingsFragment<FragmentUserSettingsUserScheduleBinding, UserSettingsViewModel>(
        R.layout.fragment_user_settings_user_schedule,
        FragmentUserSettingsUserScheduleBinding::inflate,
        sharedViewModel
    ) {
    private var scheduleAdapter: ScheduleAdapter? = null

    private var currentParent: Parent? = null

    private var from = false

    companion object {
        const val START = "start"
        const val END = "end"
    }

    override fun setUi() {
        from = arguments?.getBoolean("from") ?: false

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val act = if (from)
                        R.id.action_global_userAccountFragment
                    else
                        R.id.action_userSettingsUserScheduleFragment_to_userSettingsAddChildrenFragment
                    findNavController().navigate(act)
                }
            })

        val onShowScheduleDialog: (CareSchedule) -> Unit = { currentDay ->
            sharedViewModel.applyIntent(ViewIntent.ShowTimePickerIntent(currentDay, START))
        }

        scheduleAdapter = ScheduleAdapter(onShowScheduleDialog)
        with(binding) {
            with(rvSchedule) {
                val lManager = LinearLayoutManager(activity)
                layoutManager = lManager
                adapter = scheduleAdapter
            }

            btnNext.clicks().subscribe {
                sharedViewModel.applyIntent(ViewIntent.SaveCurrentParentIntent(currentParent))
            }
        }
    }

    private fun showTimePickerDialog(currentDay: CareSchedule, period: String) {
        val timeFormat = if (is24HourFormat(activity)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        var currentTime = listOf("0", "0")
        when (period) {
            START -> {
                if (currentDay.start != "") {
                    currentTime = currentDay.start.split(":")
                }
            }
            END -> {
                if (currentDay.end != "") {
                    currentTime = currentDay.end.split(":")
                }
            }
        }
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat)
                .setTitleText(getString(R.string.time_title, period, DayOfWeek.of(currentDay.day).name))
                .setHour(currentTime[0].toInt())
                .setMinute(currentTime[1].toInt())
                .build()
        picker.addOnPositiveButtonClickListener {
            val hour = checkTimeValue(picker.hour)
            val minute = checkTimeValue(picker.minute)
            val time = "$hour:$minute"
            when (period) {
                START -> {
                    currentDay.start = time
                    sharedViewModel.applyIntent(ViewIntent.ShowTimePickerIntent(currentDay, END))
                }
                END -> {
                    currentDay.end = time
                    sharedViewModel.applyIntent(ViewIntent.UpdateScheduleIntent(currentDay))
                }
            }
        }
        picker.show(this.childFragmentManager, "time")
    }

    private fun checkTimeValue(time: Int): String {
        return if (time < 10) "0$time" else "$time"
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.CurrentParentState -> {
                currentParent = state.parent
                val schedules = arrayListOf<CareSchedule>()
                val list = currentParent?.schedule
                if (list != null) {
                    schedules.addAll(list)
                    if (schedules.isEmpty()) {
                        for (i in 1..7) {
                            schedules.add(
                                CareSchedule(i, false, "", "")
                            )
                        }
                    }
                }
                scheduleAdapter?.schedules = schedules
            }
            else -> {}
        }
    }

    override fun performAction(action: ViewAction) {
        when (action) {
            is ViewAction.NextFragmentAction -> {
                findNavController().navigate(R.id.action_global_userAccountFragment)
            }
            is ViewAction.ShowTimePickerAction -> {
                showTimePickerDialog(action.schedule, action.period)
            }
            else -> {}
        }
    }
}