package com.example.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException

import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val OPERATOR = "Operator"
private const val OPERAND = "Operand"
private const val OPERAND_STORED = "OperandStored"

class MainActivity : AppCompatActivity() {

    // Variables for operands and type of calculations
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener { view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    newNumber.setText("")
                }
            }

        }

    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {

            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG,"onSaveInstanceState(outState:) called")
        super.onSaveInstanceState(outState)
        outState.putString(OPERATOR, pendingOperation)
        if (operand1 != null) {
            outState.putDouble(OPERAND, operand1!!)
            outState.putBoolean(OPERAND_STORED, true)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState(savedInstanceState:) called")
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(OPERATOR, "=")
        operation.text = pendingOperation
        operand1 = if (savedInstanceState.getBoolean(OPERAND_STORED, false)) {
            savedInstanceState.getDouble(OPERAND)
        } else {
            null
        }
        operand1 = savedInstanceState.getDouble(OPERAND)
    }
}