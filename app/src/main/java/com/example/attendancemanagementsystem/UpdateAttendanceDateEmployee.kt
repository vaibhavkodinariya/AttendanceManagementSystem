package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DateAttendanceAdapter
import com.example.attendancemanagementsystem.model.DateAttendance
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateAttendanceDateEmployee : AppCompatActivity() {
    private lateinit var viewGrid: GridView
    private lateinit var btnContinue: Button
    private lateinit var subjectSpinner: AutoCompleteTextView
    private lateinit var Nodata:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_attendance_date_employee)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Update Attendance for"
        Nodata=findViewById(R.id.NodataUA)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val employeeid=sharePreference.getString("id","")!!
        viewGrid=findViewById(R.id.UpdateAttendanceDateGrid)
        subjectSpinner = findViewById(R.id.UpdateAttDateAutoCompleteSubjects)

        CoroutineScope(Dispatchers.IO).launch {
                val subjects= Employees.getEmployeeSubjects(employeeid)
                subjects.also {
                    withContext(Dispatchers.Main) {
                            ArrayAdapter(
                            this@UpdateAttendanceDateEmployee,
                            android.R.layout.simple_list_item_1,
                            it
                        ).also { adapter ->
                            subjectSpinner.setAdapter(adapter)
                        }
                        var subject = ""
                        subjectSpinner.setOnItemClickListener { parent, view, position, id ->
                            val item = parent.getItemAtPosition(position)
                            subject = item.toString()
                            CoroutineScope(Dispatchers.IO).launch {
                                val dates = arrayListOf<DateAttendance>()
                                val getAttendance =
                                    Employees.getAllTakenAttendance(employeeid, subject)
                                withContext(Dispatchers.Main) {
                                if(!getAttendance.getBoolean("success")){
                                    Nodata.visibility=View.VISIBLE
                                    viewGrid.visibility= View.GONE
                                }else{
                                    viewGrid.visibility=View.VISIBLE
                                    viewGrid.visibility=View.VISIBLE
                                    Nodata.visibility=View.GONE

                                    val DateAttendaceobject = getAttendance.getJSONObject("Dates")
                                    val keyIterator = DateAttendaceobject.keys()
                                    while (keyIterator.hasNext()) {
                                        val key = keyIterator.next()
                                        val lecturesArray = Array(DateAttendaceobject.getJSONArray(key).length()) { i -> DateAttendaceobject.getJSONArray(key).getString(i) }
                                        val dateAttendance = DateAttendance(key, lecturesArray)
                                        dates.add(dateAttendance)
                                    }
                                    viewGrid.adapter =
                                        DateAttendanceAdapter(
                                            this@UpdateAttendanceDateEmployee,
                                            dates.toTypedArray(),
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