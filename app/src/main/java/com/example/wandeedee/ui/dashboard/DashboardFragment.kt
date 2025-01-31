package com.example.wandeedee.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wandeedee.DBHelper
import com.example.wandeedee.R
import com.example.wandeedee.databinding.FragmentDashboardBinding
import com.example.wandeedee.search
import com.example.wandeedee.welcome
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null



    private val binding get() = _binding!!



    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root




        var todayFortnune: TextView? = root.findViewById(R.id.todayFortnune)
        var todayFortune2 :TextView? = root.findViewById(R.id.todayFortnune2)

        val currentDate = getCurrentDate()


        todayFortnune?.text = "ดวงประจำวันที่ " + currentDate

        val dbHelper = DBHelper(requireContext())
        val cursor = dbHelper.getFortuneData(currentDate)


        if (cursor.moveToFirst()) {
            val fortuneText = cursor.getString(cursor.getColumnIndex("fortune"))

            todayFortune2?.text = fortuneText
        } else {

            todayFortune2?.text = "No fortune data available for today."
        }


        cursor.close()
        dbHelper.close()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Bangkok")
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }

}