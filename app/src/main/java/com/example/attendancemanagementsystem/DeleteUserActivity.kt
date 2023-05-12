package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.ViewEmployeeSUAdapter
import com.example.attendancemanagementsystem.adapter.ViewStudentSUAdapter
import com.example.employee.api.SuperUser
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteUserActivity : AppCompatActivity() {
//    private lateinit var view: Button
//    private lateinit var delete: Button
    private lateinit var typeSpinner: AutoCompleteTextView
    private lateinit var semesterSpinner: AutoCompleteTextView
    private lateinit var divisionSpinner: AutoCompleteTextView
    private lateinit var SemDivCard:MaterialCardView
    private lateinit var UserGrid:GridView
    private lateinit var nodata:TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="View & Delete User"
        nodata=findViewById(R.id.Nodata)
        typeSpinner=findViewById(R.id.AutoCompleteTypeOfUser)
        semesterSpinner=findViewById(R.id.AutoCompleteSemester)
        divisionSpinner=findViewById(R.id.AutoCompleteDivision)
        SemDivCard=findViewById(R.id.SemesterDivisionCard)
        UserGrid=findViewById(R.id.DeleteUserGridSU)

        val Typearry = arrayListOf("student","Teaching","NonTeaching")
        val TypeAdapter= ArrayAdapter(this@DeleteUserActivity ,android.R.layout.simple_list_item_1,Typearry)

        typeSpinner.setAdapter(TypeAdapter)

        var Type=""
        var Sem=""
        var Div=""
        typeSpinner.setOnItemClickListener { parent, view, position, id ->
            Type=Typearry[position]
            UserGrid.visibility=View.GONE
            semesterSpinner.text.clear()
            divisionSpinner.text.clear()

            if (Type=="student") {
                SemDivCard.visibility = View.VISIBLE
                val Semesterarry =
                    arrayListOf("Semester-1", "Semester-2", "Semester-3", "Semester-4")
                val SemAdapter = ArrayAdapter(
                    this@DeleteUserActivity,
                    android.R.layout.simple_list_item_1,
                    Semesterarry
                )
                semesterSpinner.setAdapter(SemAdapter)
                semesterSpinner.setOnItemClickListener { parent, view, position, id ->
                    Sem = Semesterarry[position]
                    divisionSpinner.text.clear()
                    UserGrid.visibility=View.GONE
                    val Divisionarry = arrayListOf("A", "B")
                    val DivAdapter = ArrayAdapter(
                        this@DeleteUserActivity,
                        android.R.layout.simple_list_item_1,
                        Divisionarry
                    )
                    divisionSpinner.setAdapter(DivAdapter)
                    divisionSpinner.setOnItemClickListener { parent, view, position, id ->
                        Div = Divisionarry[position]

                        CoroutineScope(Dispatchers.IO).launch {
                            val stud = SuperUser.getAllStudentData(Div, Sem)
                            withContext(Dispatchers.Main) {
                                if (stud.isEmpty()) {
                                    UserGrid.visibility=View.GONE
                                    val builder = AlertDialog.Builder(this@DeleteUserActivity)
                                    //set title for alert dialog
                                    builder.setTitle("No Data!!")
                                    //set message for alert dialog
                                    builder.setMessage("No Data Found...")
                                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                                    UserGrid.visibility=View.GONE
                                    nodata.visibility=View.VISIBLE
                                    //performing cancel action
                                    builder.setNeutralButton("Ok"){dialogInterface , which ->
                                    }
                                    // Create the AlertDialog
                                    val alertDialog: AlertDialog = builder.create()
                                    // Set other dialog properties
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                } else {
                                        UserGrid.visibility = View.VISIBLE
                                        nodata.visibility=View.GONE
                                        UserGrid.adapter =
                                            ViewStudentSUAdapter(this@DeleteUserActivity, stud)
                                }
                            }
                        }
                    }
                }
            }else{
                SemDivCard.visibility= View.GONE
                CoroutineScope(Dispatchers.IO).launch {
                    val Empl = SuperUser.getAllEmployeeData(Type)
                    withContext(Dispatchers.Main) {
                        UserGrid.visibility= View.VISIBLE
                        UserGrid.adapter = ViewEmployeeSUAdapter(this@DeleteUserActivity, Empl)
                    }
                }
                UserGrid.visibility=View.VISIBLE
            }
        }
    }
}