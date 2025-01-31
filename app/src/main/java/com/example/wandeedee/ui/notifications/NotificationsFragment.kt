package com.example.wandeedee.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wandeedee.DBHelper
import com.example.wandeedee.R
import com.example.wandeedee.databinding.FragmentNotificationsBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class NotificationsFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var down: ImageView? = null
    var pickdate: ImageView? = null
    var showdate: TextView? = null
    var SearchResult: TextView? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var startDate: String
    private lateinit var endDate: String
    var textSearchs:TextView? = null

    private var _binding: FragmentNotificationsBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = DBHelper(requireContext())

        down = root.findViewById(R.id.sBtnDown)
        SearchResult = root.findViewById(R.id.TextShowSearch)
        showdate = root.findViewById(R.id.sTextshowdate)
        pickdate = root.findViewById(R.id.sBtnSelectDate)
        textSearchs = root.findViewById(R.id.TextShowSearch)

        pickdate?.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Select Date Range")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            picker.show(childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener { dateRange ->
                startDate = convertTimeToDate(dateRange.first)
                endDate = convertTimeToDate(dateRange.second)
                showdate?.text = "$startDate - $endDate"
            }


            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }
        }
        down?.setOnClickListener {
            Toast.makeText(requireContext(), "Activity List", Toast.LENGTH_LONG).show()
        }

        return root
    }
    private fun convertTimeToDate(time: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(utc.time)
    }

    private fun showDataInTextView(data: String) {
        textSearchs?.text = "ผลการค้นหา"
        binding.SearchResult.text = data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val ausDetail = resources.getStringArray(R.array.AusDetail)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, ausDetail)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter)

        binding.autoCompleteTextView2.setOnItemClickListener { _, _, position, _ ->
            val selectedAus = ausDetail[position]

            lifecycleScope.launch {
                Log.d("DatabaseQuery", "Selected Date Range: $startDate - $endDate, Selected Activity: $selectedAus")

                val cursor = dbHelper.getAusDays(startDate, endDate, selectedAus)

                val resultList = mutableListOf<String>() // List to store results

                if (cursor.moveToFirst()) {
                    do {
                        val rangeDateIndex = cursor.getColumnIndex("Adate")
                        val ausDetailIndex = cursor.getColumnIndex("ADesc")

                        if (rangeDateIndex != -1 && ausDetailIndex != -1) {
                            val selectRDate = cursor.getString(rangeDateIndex)
                            val ausActi = cursor.getString(ausDetailIndex)

                            Log.d("DatabaseQuery", "Adate: $selectRDate, ADesc: $ausActi")

                            val displayText = "วันที่ :  $selectRDate"
                            resultList.add(displayText) // Append result to the list

                        } else {
                            Log.e("DatabaseQuery", "Column not found in the cursor.")
                        }
                    } while (cursor.moveToNext())
                }


                cursor.close()

                if (resultList.isNotEmpty()) {

                    val resultText = resultList.joinToString("\n")
                    showDataInTextView(resultText)
                } else {
                    showDataInTextView("No data found for the selected date and activity.")
                }
            }

        }


}

    }
