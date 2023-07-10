package com.example.firstcalculator

import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.firstcalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var clicks:Int = 0
    private var results:Int = 0
    var lastNumeric = false
    var lastDot = false
    var stateError = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onAllclearClick(view: View) {
        clicks = 0
        //Resetting each and everything on this
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        lastDot = false
        lastNumeric = false
        stateError = false
    }

    fun onEqualClick(view: View) {
        if(clicks == 0){
            onEqual()
            //binding.dataTv.text = binding.resultTv.text.toString().drop(1)
            clicks++
            results++
        }
    }

    fun onDigitClick(view: View) {
        if(results==1){
            binding.dataTv.text = ""
            binding.resultTv.text = ""
        }
        results = 0
        clicks = 0
        if(stateError){

           binding.dataTv.text = (view as Button).text
            stateError = false

        }
        else {
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        //onEqual()
    }

    fun onOperatorClick(view: View) {
        clicks = 0
        if(!stateError && lastNumeric){
            binding.dataTv.append((view as Button).text)
            lastNumeric = false
            lastDot = false
            //onEqual()
            clicks = 0
        }
    }

    fun onBackClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        clicks = 0
        try{
            val lastChar = binding.dataTv.text.toString().last()
            //if(lastChar.isDigit()){
               //onEqual()
            //}
        }
        catch (e : Exception){
            binding.resultTv.text = ""
            //binding.resultTv.visibility = View.GONE
            Log.e("Last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
        clicks = 0
    }

    fun onEqual(){
            //clicks += 1
            if(lastNumeric && !stateError) {
                val txt = binding.dataTv.text.toString()
                expression = ExpressionBuilder(txt).build()
                try {
                    val result = expression.evaluate()
                    val formattedResult = if (result % 1 == 0.0) {
                        result.toInt().toString() // Convert to integer if it's a whole number
                    } else {
                        DecimalFormat("#.#############").format(result) // Format with two decimal places
                    }
                    binding.resultTv.visibility = View.VISIBLE
                    binding.resultTv.text = formattedResult
                }
                catch (ex : ArithmeticException) {
                    Log.e("Evaluate Error", ex.toString())
                    binding.resultTv.text = "Error"
                    stateError = true
                    lastNumeric = false
                }
            }
    }
}