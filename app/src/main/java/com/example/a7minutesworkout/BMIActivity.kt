package com.example.a7minutesworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minutesworkout.databinding.ActivityBmiActivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }

    private var currentVisibleView:String = METRIC_UNITS_VIEW //to make selected view visible
    private var binding: ActivityBmiActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)//Activates the back button
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        makeMetricUnitsViewVisible()

        binding?.rgUnits?.setOnCheckedChangeListener{_,checkedId:Int ->
            if(checkedId == R.id.rbMetricUnits){
                makeMetricUnitsViewVisible()
            }else{
                makeUsUnitsViewVisible()
            }
        }
        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
    }
    private fun makeMetricUnitsViewVisible(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE

        binding?.tilUsMetricUnitWeight?.visibility = View.GONE
        binding?.tiMetricUnitHeightFeet?.visibility = View.GONE
        binding?.tiMetricUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }
    private fun makeUsUnitsViewVisible(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE

        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tiMetricUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tiMetricUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricHeightFeet?.text!!.clear()
        binding?.etUsMetricHeightInch?.text!!.clear()
        binding?.etUsMetricUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }
    private fun displayBMIResult(bmi:Float){
        val bmiLabel:String
        val bmiDescription:String
        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Moderately obese"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Severely obese"
            bmiDescription = "OMG! You are in a very dangerous condition! Act Now!"
        }else {
            bmiLabel = "Very Severely Overweight"
            bmiDescription = "OMG! You are in a very dangerous condition! Act Now!"
        }
        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2,RoundingMode.HALF_EVEN).toString()
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }
    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }
    private fun calculateUnits(){
         if(currentVisibleView == METRIC_UNITS_VIEW){
             if (validateMetricUnits()) {
                 val heightValue: Float =
                     binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                 val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                 val bmi = weightValue / (heightValue * heightValue)
                 displayBMIResult(bmi)
             } else {
                 Toast.makeText(
                     this@BMIActivity,
                     "Please enter valid values",
                     Toast.LENGTH_SHORT
                 ).show()
             }
         }else{
             if(validateUsUnits()){
                 val usUnitHeightValueFeet:String =
                     binding?.etUsMetricHeightFeet?.text.toString()
                 val usUnitHeightValueInch:String =
                     binding?.etUsMetricHeightInch?.text.toString()
                 val usUnitWeightValue:Float =
                     binding?.etUsMetricUnitWeight?.text.toString().toFloat()
                 val heightValue=
                     usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                 val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                 displayBMIResult(bmi)
             }else {
                 Toast.makeText(
                     this@BMIActivity,
                     "Please enter valid values",
                     Toast.LENGTH_SHORT
                 ).show()
             }
         }
    }
    private fun validateUsUnits(): Boolean {
        var isValid = true

        when{
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsMetricHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsMetricHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }

        return isValid
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}