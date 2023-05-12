package com.example.attendancemanagementsystem

import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DailyAttendanceAdapter
import com.example.employee.api.Students
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DailyAttendanceActivity : AppCompatActivity() {
    private lateinit var dailyAttendance: GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Daily Attendance"

        dailyAttendance=findViewById(R.id.ViewAttendanceStudent)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val enrollmentno=sharePreference.getString("id","")!!
        CoroutineScope(Dispatchers.IO).launch {
            val attendances= Students.getDailyAttendance(enrollmentno)
            if(attendances.isEmpty()){
            }else {
                withContext(Dispatchers.Main) {
                    dailyAttendance.adapter =
                        DailyAttendanceAdapter(this@DailyAttendanceActivity, attendances)
                }
            }
        }
    }
}