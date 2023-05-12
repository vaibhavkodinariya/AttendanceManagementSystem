package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.ViewAttendanceEmployeeAdapter
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectDetailsViewAttendanceActivity : AppCompatActivity() {
    private lateinit var subjectSpinner: AutoCompleteTextView
    private lateinit var viewGrid: GridView
    private lateinit var NoData:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_details_view_attendance)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="View Attendance"
        NoData=findViewById(R.id.NodataVA)
        subjectSpinner = findViewById(R.id.ViewAttendanceAutoCompleteSubjects)
        viewGrid=findViewById(R.id.ViewAttendaceEmployeeGridView)

        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val employeeid=sharePreference.getString("id","")!!
        var subject = ""
        CoroutineScope(Dispatchers.IO).launch {
            val subjects= Employees.getEmployeeSubjects(employeeid)
            subjects.also {
                    withContext(Dispatchers.Main) {
                        if (subjects.isEmpty()){
                        }else {

                            ArrayAdapter(
                            this@SelectDetailsViewAttendanceActivity,
                            android.R.layout.simple_list_item_1,
                            it
                        ).also { adapter ->
                            subjectSpinner.setAdapter(adapter)
                        }
                        subjectSpinner.setOnItemClickListener { parent, view, position, id ->
                            val item = parent.getItemAtPosition(position)
                            subject = item.toString()
                            CoroutineScope(Dispatchers.IO).launch {
                                val studentsByDivision=Employees.getStudentByDivision(subject,employeeid)
                                withContext(Dispatchers.Main) {
                                    if (studentsByDivision.isEmpty()){
                                        viewGrid.visibility= View.GONE
                                        NoData.visibility=View.VISIBLE
                                    }else {
                                        NoData.visibility=View.GONE
                                        viewGrid.visibility= View.VISIBLE
                                        viewGrid.adapter = ViewAttendanceEmployeeAdapter(
                                            this@SelectDetailsViewAttendanceActivity,
                                            studentsByDivision,
                                            subject
                                        )
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}
