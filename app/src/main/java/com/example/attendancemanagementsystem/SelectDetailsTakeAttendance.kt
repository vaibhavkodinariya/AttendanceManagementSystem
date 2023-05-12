package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagementsystem.adapter.AttendanceAdapter
import com.example.attendancemanagementsystem.model.Student
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SelectDetailsTakeAttendance : AppCompatActivity() {
        private lateinit var btnContinue:Button
        private lateinit var subjectSpinner:AutoCompleteTextView
        private lateinit var divisionSpinner:AutoCompleteTextView
        private lateinit var date:TextView
        private lateinit var currentdate:SimpleDateFormat
        private lateinit var attendanceGrid: RecyclerView
        private lateinit var SelectAllCheckBox:CheckBox
        private lateinit var submitAttendance: Button
        private lateinit var NoData:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_details_take_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Take Attendance"

        attendanceGrid=findViewById(R.id.takeStudent)
        SelectAllCheckBox=findViewById(R.id.CheckboxAll)
        submitAttendance=findViewById(R.id.submitAttendance)
        NoData=findViewById(R.id.NoDataId)

        subjectSpinner = findViewById(R.id.AutoCompleteSubjects)
        date=findViewById(R.id.date)
        currentdate = SimpleDateFormat("ddMMMMyyyy")
        date.text=currentdate.format(Date())
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val employeeid=sharePreference.getString("id","")!!
        var subject = ""
        SelectAllCheckBox.visibility=View.GONE
        submitAttendance.visibility=View.GONE
        CoroutineScope(Dispatchers.IO).launch {
                val subjects=Employees.getEmployeeSubjects(employeeid)
                subjects.also {
                    withContext(Dispatchers.Main) {
                        if(subjects.isEmpty()){
                        }else {
                        ArrayAdapter(
                            this@SelectDetailsTakeAttendance,
                            android.R.layout.simple_list_item_1,
                            it
                        ).also { adapter ->
                            subjectSpinner.setAdapter(adapter)
                        }
                        subjectSpinner.setOnItemClickListener { parent, view, position, id ->
                            val item = parent.getItemAtPosition(position)
                            subject = item.toString()
                            CoroutineScope(Dispatchers.IO).launch {
                                val students= Employees.getStudentByDivision(subject,employeeid)
                                withContext(Dispatchers.Main) {
                                    if (students.isEmpty()){
                                        SelectAllCheckBox.visibility= View.GONE
                                        submitAttendance.visibility= View.GONE
                                        NoData.visibility= View.VISIBLE
                                        attendanceGrid.visibility=View.GONE
                                    }else {
                                        NoData.visibility= View.GONE
                                        SelectAllCheckBox.visibility= View.VISIBLE
                                        submitAttendance.visibility= View.VISIBLE
                                        attendanceGrid.visibility=View.VISIBLE
                                        val adapter = AttendanceAdapter(
                                            this@SelectDetailsTakeAttendance,
                                            students,
                                            ::studentCheckboxClicked,
                                            SelectAllCheckBox,
                                            subject,
                                            date.text.toString(),
                                            submitAttendance
                                        )
                                        SelectAllCheckBox.setOnClickListener {
                                            selectAllCheckBoxClicked(
                                                students,
                                                adapter
                                            )
                                        }
                                        attendanceGrid.layoutManager = GridLayoutManager(
                                            this@SelectDetailsTakeAttendance,
                                            students.size + 1,
                                            RecyclerView.HORIZONTAL,
                                            false
                                        )
                                        attendanceGrid.adapter = adapter
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private fun studentCheckboxClicked(student: Student, checked: Boolean)
    {
        student.isChecked = checked
    }

    private fun selectAllCheckBoxClicked(students:Array<Student>, studentsAdapter:RecyclerView.Adapter<AttendanceAdapter.ViewHolder>)
    {
        students.forEach { student -> student.isChecked = SelectAllCheckBox.isChecked }
        studentsAdapter.notifyItemRangeChanged(0, students.size)
    }
}