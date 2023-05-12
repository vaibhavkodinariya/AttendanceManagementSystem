package com.example.attendancemanagementsystem

import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.ViewAttendanceEmployeeAdapter
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewAttendanceEmployee : AppCompatActivity() {
    private lateinit var viewGrid:GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance_employee)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="View Attendance"

        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val id=sharePreference.getString("id","")!!
        var subject=intent.getStringExtra("subject")
        viewGrid=findViewById(R.id.ViewAttendaceEmployeeGridView)

        CoroutineScope(Dispatchers.IO).launch {
                val studentsByDivision=Employees.getStudentByDivision(subject,id)
                withContext(Dispatchers.Main) {
                    if (studentsByDivision.isEmpty()){

                    }else {
                    viewGrid.adapter = ViewAttendanceEmployeeAdapter(
                        this@ViewAttendanceEmployee,
                        studentsByDivision,
                        subject
                    )
                }
            }
        }
    }
}