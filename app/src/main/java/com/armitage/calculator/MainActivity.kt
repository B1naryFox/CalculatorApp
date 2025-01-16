package com.armitage.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    val calculationStack = mutableListOf<String>()
    lateinit var textViewCalculation: TextView
    var stringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCalculation = findViewById(R.id.textViewCalculation)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    /**
     * When clicking a button with a number or the decimal point the character will be appended to
     * the String in the TextView
     * @param view
     */
    fun btnClickNum(view: View) {
        val btn = findViewById<Button>(view.id)
        stringBuilder.append(textViewCalculation.text)
        stringBuilder.append(btn.text)
        textViewCalculation.text = stringBuilder.toString()
        stringBuilder.clear()
    }
}



