package com.example.wandeedee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class welcome : AppCompatActivity() {

    var mainWelcome : Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)
        mainWelcome = findViewById(R.id.wel_btn)
        // เป็นการดึงข้อมูลการ login จาก Firebase Authentication


        mainWelcome?.setOnClickListener {
            startActivity(Intent(this@welcome,mainHome::class.java))
            finish()
        }
    }
}