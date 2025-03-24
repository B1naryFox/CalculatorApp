package com.armitage.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.notkamui.keval.Keval
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    var calculationStack = mutableListOf<String>()
    lateinit var textViewCalculation: TextView
    lateinit var textViewHistory: TextView
    var stringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCalculation = findViewById(R.id.textViewCalculation)
        textViewHistory = findViewById(R.id.textViewHistory)
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

    }

    private fun calculateEquation (calculationStack : MutableList<String>) : MutableList<String>{
        var i :Int = 0
        while (calculationStack.size > 1){

            if(calculationStack[i+1] in arrayOf("sin", "cos", "tan", "sqrt")){
                val temp = calculateSingleTerm(calculationStack[i].toDouble(), calculationStack[i+1])
                calculationStack.removeAt(i)
                calculationStack[i] = temp.toString()
            }

            else if (calculationStack[i+2] in arrayOf("+", "-", "*", "/")){
                val temp = calculateSingleTerm(calculationStack[i].toDouble(), calculationStack[i+1].toDouble(), calculationStack[i+2])
                calculationStack.removeAt(i)
                calculationStack.removeAt(i)
                calculationStack[i] = temp.toString()
            }

            else if (calculationStack[i+2] in arrayOf("sin", "cos", "tan", "sqrt")){
                val temp = calculateSingleTerm(calculationStack[i+1].toDouble(), calculationStack[i+2])
                calculationStack.removeAt(i+1)
                calculationStack[i+1] = temp.toString()
            }
        }

        return calculationStack
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
            "sin" -> return sin(operand)
            "cos" -> return cos(operand)
            "tan" -> return tan(operand)
            "sqrt" -> return sqrt(operand)
        }
        return 0.0
    }
}



