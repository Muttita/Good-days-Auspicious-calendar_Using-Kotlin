package com.example.wandeedee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.Pair
import com.example.wandeedee.ui.dashboard.DashboardFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class search : AppCompatActivity() {
    var down:ImageView? = null
    var back:ImageView? = null
    var pickdate:ImageView? = null
    var showdate:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        pickdate?.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Select Date Range")
                .setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
                .build()

            picker.show(this.supportFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                showdate?.setText(convertTimeToDate(it.first)+ " - "+convertTimeToDate(it.second))
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }
        }
        down?.setOnClickListener{
            Toast.makeText(this,"Activity lists",Toast.LENGTH_LONG).show()
        }
        back?.setOnClickListener{
            startActivity(Intent(this@search, mainHome::class.java))
            finish()
        }
    }

    fun init(){
        down = findViewById(R.id.sBtnDown)
        back = findViewById(R.id.sBtn_back)
        pickdate = findViewById(R.id.sBtnSelectDate)
        showdate = findViewById(R.id.sTextshowdate)
    }

    private fun convertTimeToDate(time: Long): String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(utc.time)
    }
}