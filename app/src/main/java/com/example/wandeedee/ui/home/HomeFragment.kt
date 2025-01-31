package com.example.wandeedee.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wandeedee.DBHelper
import com.example.wandeedee.R
import com.example.wandeedee.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var selectDateView:TextView? = null
    var calendarView:CalendarView? = null
    private lateinit var dbHelper: DBHelper
    var ShowDateTxt:TextView? = null


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        selectDateView = root.findViewById(R.id.selectedDateTextView)
        calendarView = root.findViewById(R.id.calendarView)
        ShowDateTxt = root.findViewById(R.id.ShowDateTxt)
        dbHelper = DBHelper(requireContext())


        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val selectedDate = formatDate(year, month, dayOfMonth)
            ShowDateTxt?.text = "วันที่ : " + selectedDate

            // Query the database using DBHelper
            val cursor = dbHelper.getCalenderData(selectedDate)

            // Check if the cursor has data
            if (cursor.moveToFirst()) {

                val descIndex = cursor.getColumnIndex("ADesc")
                val detailIndex = cursor.getColumnIndex("ADetail")

                if (descIndex >= 0 && detailIndex >= 0) {

                    val description = cursor.getString(descIndex)
                    val detail = cursor.getString(detailIndex)

                    val formattedDetail = if (detail != null) detail else ""

                    selectDateView?.text = "$description\n$formattedDetail"
                } else {

                    selectDateView?.text = "Invalid column indices"
                }
            } else {

                selectDateView?.text = "ไม่มีฤกษ์ดี"
            }

            cursor.close()
        }


        return root
    }


    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


