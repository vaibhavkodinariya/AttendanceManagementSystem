package com.example.attendancemanagementsystem

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.employee.api.Employees
import com.example.employee.api.Students
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BarGraphActivity : AppCompatActivity() {
    private val width = 600.0
    private val height = 400.0
    private lateinit var Heading: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_graph)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Heading = findViewById(R.id.idTVHead)
        Heading.text = "Attendance"
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val id = sharePreference.getString("id", "")!!
        val types = sharePreference.getString("type", "")!!
        var data = ""
        if (types.equals("student")) {
            data = "students"
        } else if (types.equals("Teaching")) {
            data = "Teaching"
        } else {
            data = "superAdmin"
        }
        var subject = intent.getStringExtra("subject")
        var enrollmentno = intent.getStringExtra("enrollmentno")
        var month = intent.getStringExtra("month")
        CoroutineScope(Dispatchers.IO).launch {
            val percentage = if (data == "Teaching") {
                Employees.getStudentAttendance(id, subject, enrollmentno)
            } else if (data == "students" && !month.equals(null)) {
                Students.getStudentAttendanceByMonth(id, subject, month)
            } else if (data == "superAdmin") {
                Students.getAttendance(subject, enrollmentno, data)
            } else {
                Students.getAttendance(subject, id, data)
            }
            withContext(Dispatchers.Main) {
                if (!percentage.getBoolean("success")) {

                } else {
                        val barData: BarData
                        val barDataSet: BarDataSet
                        val barEntriesList: ArrayList<BarEntry>
                        val barChart = findViewById<BarChart>(R.id.idBarChart)
                        val total = percentage.getInt("TotalNumberOfLectures")
                        val percent = percentage.getInt("attendancePercentage")
                        val attended = percentage.getInt("studentAttendLecture")
                        val absent = total - attended
                        barEntriesList = ArrayList()
                        barEntriesList.add(BarEntry(5f, 0f))
                        barEntriesList.add(BarEntry(6f, absent.toFloat()))
                        barEntriesList.add(BarEntry(8f, attended.toFloat()))
                        barEntriesList.add(BarEntry(10f, total.toFloat()))
                        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")
                        barData = BarData(barDataSet)
                        barChart.data = barData
                        barDataSet.valueTextColor = Color.BLACK
                        barDataSet.setColor(R.color.purple_200)
                        barDataSet.valueTextSize = 16f

                        if (month != null) {
                            "Percentage of attendance :- $percent% Attendance of $month month".also {
                                barChart.description.text = it
                            }
                        } else {
                            "Percentage of attendance :- $percent%".also {
                                barChart.description.text = it
                            }
                        }
                        barChart.description.textSize = 16f
                        barChart.description.isEnabled = true
                        barChart.invalidate()
                    }
            }
        }
    }
}

