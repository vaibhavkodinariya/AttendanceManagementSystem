package com.example.attendancemanagementsystem

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.example.attendancemanagementsystem.adapter.AttendanceAdapter
import com.example.attendancemanagementsystem.model.Student
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TakeAttendanceActivity : AppCompatActivity() {
    private lateinit var attendanceGrid:RecyclerView
    private lateinit var SelectAllCheckBox:CheckBox
    private lateinit var submitAttendance: Button
    private lateinit var NoData:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Take Attendance"
        attendanceGrid=findViewById(R.id.takeStudent)
        SelectAllCheckBox=findViewById(R.id.CheckboxAll)
        submitAttendance=findViewById(R.id.submitAttendance)
        NoData=findViewById(R.id.NoDataId)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val id=sharePreference.getString("id","")

        val subject=intent.getStringExtra("subject")
        val date=intent.getStringExtra("date")
        CoroutineScope(Dispatchers.IO).launch {
                val students= Employees.getStudentByDivision(subject,id)
                withContext(Dispatchers.Main) {
                    if (students.isEmpty()){
                        SelectAllCheckBox.visibility= View.GONE
                        submitAttendance.visibility=View.GONE
                        NoData.visibility=View.VISIBLE
                    }else {
                    val adapter = AttendanceAdapter(
                        this@TakeAttendanceActivity,
                        students,
                        ::studentCheckboxClicked,
                        SelectAllCheckBox,
                        subject,
                        date,
                        submitAttendance
                    )
                    SelectAllCheckBox.setOnClickListener {
                        selectAllCheckBoxClicked(
                            students,
                            adapter
                        )
                    }
                    attendanceGrid.layoutManager = GridLayoutManager(
                        this@TakeAttendanceActivity,
                        students.size + 1,
                        HORIZONTAL,
                        false
                    )
                    attendanceGrid.adapter = adapter
                }
            }
        }
    }
    private fun studentCheckboxClicked(student: Student, checked: Boolean)
    {
        student.isChecked = checked
    }

    private fun selectAllCheckBoxClicked(students:Array<Student>,studentsAdapter:RecyclerView.Adapter<AttendanceAdapter.ViewHolder>)
    {
        students.forEach { student -> student.isChecked = SelectAllCheckBox.isChecked }
        studentsAdapter.notifyItemRangeChanged(0, students.size)
    }
}