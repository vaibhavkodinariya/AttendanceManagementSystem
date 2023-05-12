package com.example.attendancemanagementsystem

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DropDownEmployeeAdapter
import com.example.attendancemanagementsystem.adapter.DropDownSubjectAdapter
import com.example.attendancemanagementsystem.adapter.StudentQueriesAdapter
import com.example.attendancemanagementsystem.model.Queries
import com.example.employee.api.Employees
import com.example.employee.api.Students
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentQueryActivity : AppCompatActivity() {
    private lateinit var query: GridView
    private lateinit var Send: Button
    private lateinit var subSpinner: AutoCompleteTextView
    private lateinit var employeesSpinner: AutoCompleteTextView
    private lateinit var Description: EditText
    private lateinit var dropdownEmployee: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_query)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Queries"
        Send = findViewById(R.id.Send)
        query = findViewById(R.id.ViewQueryGridView)
        subSpinner = findViewById(R.id.AutoCompleteSubjectsQuery)
        employeesSpinner = findViewById(R.id.AutoCompleteEmployeeQuery)
        Description = findViewById(R.id.DiscriptionField)
        dropdownEmployee=findViewById(R.id.QueryEmployeeDropdown)
        descriptionLayout=findViewById(R.id.Discription)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val userId = sharePreference.getString("id", "")!!
        val types = sharePreference.getString("type", "")!!
        var data=""
        if (!types.equals("student")){
            data = "employees"
        }else{
            data = "students"
        }

        var employeeId=""
        var subjectid=""
        var semesterid=""

        CoroutineScope(Dispatchers.IO).launch {
            val subjects = Students.getSubjectsByStudent(userId)
            subjects.also {
                withContext(Dispatchers.Main) {

                    var adapter = DropDownSubjectAdapter(this@StudentQueryActivity,subjects)
                    subSpinner.setAdapter(adapter)
                    dropdownEmployee.visibility=View.GONE
                    descriptionLayout.visibility=View.GONE
                    subSpinner.setOnItemClickListener { parent, view, position, id ->
                        subjectid=subjects[position].subjectid.toString()
                        semesterid=subjects[position].semesterid.toString()
                        dropdownEmployee.visibility= View.VISIBLE
                        subSpinner.setOnFocusChangeListener { v, hasFocus ->
                            employeesSpinner.text.clear()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            val employees = Employees.getEmployeesBySubject(subjectid)
                            withContext(Dispatchers.Main){
                                val employeeadapter= DropDownEmployeeAdapter(this@StudentQueryActivity,employees)
                                employeesSpinner.setAdapter(employeeadapter)
                                employeesSpinner.setOnItemClickListener { parent, view, position, id ->
                                    descriptionLayout.visibility=View.VISIBLE
                                    employeeId=employees[position].employeeid.toString()
                                }
                            }
                        }
                    }
                }
                Send.setOnClickListener {
                    if (TextUtils.isEmpty(subSpinner.text)&& TextUtils.isEmpty(employeesSpinner.text)&& TextUtils.isEmpty(Description.text))
                    {
                        val builder = AlertDialog.Builder(this@StudentQueryActivity)
                        //set title for alert dialog
                        builder.setTitle("Warning !")
                        //set message for alert dialog
                        builder.setMessage("Please Fill Data properly ..!")
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        //performing cancel action
                        builder.setNeutralButton("Ok"){dialogInterface , which ->
                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    }
                    else if(TextUtils.isEmpty(subSpinner.text)|| TextUtils.isEmpty(employeesSpinner.text)|| TextUtils.isEmpty(Description.text)){
                        val builder = AlertDialog.Builder(this@StudentQueryActivity)
                        //set title for alert dialog
                        builder.setTitle("Warning !")
                        //set message for alert dialog
                        builder.setMessage("Please Fill Data properly ..!")
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        //performing cancel action
                        builder.setNeutralButton("Ok"){dialogInterface , which ->
                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    }
                    else{
                        val builder = AlertDialog.Builder(this@StudentQueryActivity)

                        //set title for alert dialog
                        builder.setTitle("Attention please")
                        //set message for alert dialog
                        builder.setMessage("Do You Want To Send This Query?")
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        //performing cancel action
                        builder.setPositiveButton("Yes"){dialogInterface , which ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val response=Employees.sendQuery(Description.text.toString(),employeeId,userId,subjectid,semesterid,data)
                                withContext(Dispatchers.Main){
                                    if (response.getBoolean("success")){
                                        Toast.makeText(this@StudentQueryActivity,response.getString("messege"),Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@StudentQueryActivity,response.getString("messege"),Toast.LENGTH_SHORT).show()
                                    }
                                    subSpinner.text.clear()
                                    employeesSpinner.text.clear()
                                    Description.text.clear()
                                    this@StudentQueryActivity.recreate()
                                }
                            }
                        }
                        //performing negative action
                        builder.setNegativeButton("No"){dialogInterface, which ->

                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.show()

                    }
                }
            }
        }
        val queriesArrayList = arrayListOf<Queries>()
        CoroutineScope(Dispatchers.IO).launch {
            var queries = Students.getQueries(userId)
            if(queries.getBoolean("success")) {

            val studentArray = queries.getJSONArray("queries")

            var i = 0
            while (i < studentArray.length()) {
                 val data = studentArray.getJSONObject(i)
                 val studentQueries = Queries(
                    data.getString("semestername"),
                    data.getString("subjectName"),
                    data.getString("description"),
                    data.getString("firstname"),
                    data.getString("lastname"),
                    data.getString("middlename"),
                    null
                    )
                 queriesArrayList.add(studentQueries)
                 i++
            }
                withContext(Dispatchers.Main){
                    query.adapter= StudentQueriesAdapter(this@StudentQueryActivity,queriesArrayList.toTypedArray())
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@StudentQueryActivity, queries.getString("messege"), Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
