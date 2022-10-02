package com.example.a7minutesworkout


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarFinishActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)//Activates the back button
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnFinish?.setOnClickListener {
            finish()
        }
        val historyDao = (application as WorkOutApp).db.historyDao()
        addDateToDatabase(historyDao)
    }

    private fun addDateToDatabase(historyDao: HistoryDao) {
        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.d("Date: ", "" + dateTime)
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.d("Formatted Date: ", "" + date)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e(
                "Date: ",
                "Added... "
            )
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}