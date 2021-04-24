package com.websarva.wings.android.hobbymanagementapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)

        val text = intent.getStringExtra("text")
        val tvDebug = findViewById<TextView>(R.id.tvDebug)

        tvDebug.text = text
    }

    fun onBackButtonClick(view: View){
        finish()
    }
}