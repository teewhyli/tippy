package com.example.tippy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.animation.ArgbEvaluator
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val BASE_HUNDRED = 100.0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etBase:EditText = findViewById(R.id.etBase)
        val tvTipAmount: TextView = findViewById(R.id.tvTipAmount)
        val tvTotalAmount: TextView = findViewById(R.id.tvTotalAmount)
        val tvTipPercent:TextView = findViewById(R.id.tvTipPercent)
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        val seekBarTip:SeekBar = findViewById(R.id.seekBarTip)
        seekBarTip.progress = INITIAL_TIP_PERCENT

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress%")
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress, seekBarTip)
                computeTipAndTotal(etBase, seekBarTip, tvTipAmount, tvTotalAmount)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBase.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal(etBase, seekBarTip, tvTipAmount, tvTotalAmount)
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int, seekBarTip: SeekBar) {
        val tvTipDescription: TextView = findViewById(R.id.tvTipDescription)
        val tipDescription: String = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing!"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal(etBase: EditText, seekBarTip: SeekBar, tvTipAmount: TextView, tvTotalAmount:TextView) {
        //Get the value of the base and tip percent
        val baseAmount: Double = if(etBase.text.isNullOrEmpty()){
            0.0
        } else {
            etBase.text.toString().toDouble()
        }
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / BASE_HUNDRED
        val totalAmount = baseAmount + tipAmount
        tvTipAmount.text = tipAmount.toString()
        tvTotalAmount.text = totalAmount.toString()
    }
}