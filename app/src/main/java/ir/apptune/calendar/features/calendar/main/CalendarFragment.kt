package ir.apptune.calendar.features.calendar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.apptune.calendar.R
import ir.apptune.calendar.pojo.CalendarModel
import ir.apptune.calendar.utils.SELECTED_DAY_DETAILS
import ir.apptune.calendar.utils.extensions.toPersianMonth
import ir.apptune.calendar.utils.extensions.toPersianNumber
import ir.apptune.calendar.utils.extensions.toPersianWeekDay
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class CalendarFragment : Fragment() {

    private val viewModel: CalendarViewModel by viewModel()
    private val today: CalendarModel by inject()
    private val adapter: CalendarAdapter = CalendarAdapter {
        val data = Bundle().apply {
            putParcelable(SELECTED_DAY_DETAILS, it)
        }
        findNavController().navigate(R.id.action_calendarFragment_to_onClickDialogActivity, data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.calendar_fragment, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpToolbarTexts()

        viewModel.getMonthLiveData().observe(viewLifecycleOwner, {
            showCalendar(it.toMutableList())
        })
        recyclerCalendar.adapter = adapter
        recyclerCalendar.itemAnimator = null
        imgNextMonth.setOnClickListener { viewModel.getNextMonth() }
        imgPreviousMonth.setOnClickListener { viewModel.getPreviousMonth() }
    }

    private fun setUpToolbarTexts() = with(today) {
        txtWeekDay.text = dayOfWeek.toPersianWeekDay(requireContext())
        txtMonthDate.text = iranianDay.toPersianNumber()
        txtCurrentMonth.text = iranianMonth.toPersianMonth(requireContext())
    }

    private fun showCalendar(list: List<CalendarModel>) {
        txtMonthName.text = list.last().iranianMonth.toPersianMonth(requireContext())
        txtYear.text = list.last().iranianYear.toPersianNumber()
        adapter.submitList(list)
    }


}