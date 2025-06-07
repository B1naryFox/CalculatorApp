package com.armitage.calculatorapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
import androidx.core.view.isGone

class MainActivity : AppCompatActivity() {

    var calculationStack = mutableListOf<String>()
    private val memory = mutableListOf<String>("0", "0")

    lateinit var textViewCalculation: TextView
    lateinit var textViewHistory: TextView

    lateinit var scientificFunction: TableRow

    var stringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        textViewCalculation = findViewById(R.id.textViewCalculation)
        textViewHistory = findViewById(R.id.textViewHistory)

        scientificFunction = findViewById(R.id.scientificFunctions)

        val memorySaveBtn = findViewById<Button>(R.id.btnMemorySave)
        memorySaveBtn.setOnClickListener { saveToMemory(0) }
        memorySaveBtn.setOnLongClickListener { saveToMemory(1) }

        val memoryReadBtn = findViewById<Button>(R.id.btnMemoryRead)
        memoryReadBtn.setOnClickListener {
            readFromMemory(0)
        }
        memoryReadBtn.setOnLongClickListener {
            readFromMemory(1)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (findViewById<View>(R.id.portraitLayout) != null)
            menuInflater.inflate(R.menu.options_menu, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSettings -> {
                if (scientificFunction.isGone) scientificFunction.visibility = View.VISIBLE
                else scientificFunction.visibility = View.GONE
                true
            } else -> super.onOptionsItemSelected(item)
        }
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

    fun btnClickEnter(view: View){
        stringBuilder.append(textViewCalculation.text)
        stringBuilder.append("_")
        textViewCalculation.text = stringBuilder.toString()
        stringBuilder.clear()
    }

    private fun textToList(text: String) : MutableList<String> {
        val tempStack = mutableListOf<String>()
        val list : List<String> = text.split('_')
        for (substring in list){
            tempStack.add(substring)
        }
        return tempStack
    }

    fun btnClickEquals(view: View){
        try {
            calculationStack = textToList(textViewCalculation.text.toString())
            val result = calculateEquation(calculationStack).last().toString()
            stringBuilder.append(textViewHistory.text)
            stringBuilder.append(textViewCalculation.text)
            stringBuilder.append("\n=")
            stringBuilder.append(result)
            stringBuilder.append("\n")
            textViewHistory.text = stringBuilder.toString()
            stringBuilder.clear()

            stringBuilder.append(result)
            textViewCalculation.text = stringBuilder.toString()
            stringBuilder.clear()
        } catch (e: Exception) {
            btnClickClear(view)
            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
        }
    }

    fun btnClickClearEntry(view: View) {
        stringBuilder.append(textViewCalculation.text.toString())
        if(stringBuilder.isNotEmpty()){
            stringBuilder.deleteCharAt(stringBuilder.length - 1);
        }
        textViewCalculation.text = stringBuilder.toString()
        stringBuilder.clear()
    }
    fun btnClickClear(view: View) {
        textViewCalculation.text = ""
    }

    private fun calculateEquation(calculationStack: MutableList<String>): MutableList<String> {
        val stack = mutableListOf<Double>()

        try {
            for (token in calculationStack) {
                when {
                    token.toDoubleOrNull() != null -> {
                        stack.add(token.toDouble())
                    }
                    token in arrayOf("sin", "cos", "tan", "sqrt") -> {
                        if (stack.isEmpty()) throw IllegalArgumentException("Insufficient operands for '$token'")
                        val operand = stack.removeAt(stack.lastIndex)
                        stack.add(calculateSingleTerm(operand, token))
                    }
                    token in arrayOf("+", "-", "*", "/") -> {
                        if (stack.size < 2) throw IllegalArgumentException("Insufficient operands for '$token'")
                        val operandB = stack.removeAt(stack.lastIndex)
                        val operandA = stack.removeAt(stack.lastIndex)
                        stack.add(calculateSingleTerm(operandA, operandB, token))
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown token: $token")
                    }
                }
            }

            return stack.map { it.toString() }.toMutableList()
        } catch (e: Exception) {
            throw Exception("Error while calculating: ${e.message}", e)
        }
    }


    private fun calculateSingleTerm (operandA: Double, operandB: Double, operator: String) : Double{
        when (operator){
            "+" -> return operandA.plus(operandB)
            "-" -> return operandA.minus(operandB)
            "*" -> return operandA.times(operandB)
            "/" -> {
                if (operandB.equals(0.0)) throw IllegalArgumentException("Error: Cannot divide by 0")
                return operandA.div(operandB)
            }
        }
        return 0.0
    }

    private fun calculateSingleTerm (operand: Double, operator: String) : Double{
        when(operator){
            "sin" -> return sin(Math.toRadians(operand))
            "cos" -> return cos(Math.toRadians(operand))
            "tan" -> return tan(Math.toRadians(operand))
            "sqrt" -> return sqrt(operand)
        }
        return 0.0
    }
    
    private fun saveToMemory(int: Int): Boolean {
        memory[int] = textViewCalculation.text.toString()
        return true
    }
    private fun readFromMemory(int: Int): Boolean {
        stringBuilder.append(textViewCalculation.text)
        stringBuilder.append(memory[int])
        textViewCalculation.text = stringBuilder.toString()
        stringBuilder.clear()
        return true
    }


    
}



