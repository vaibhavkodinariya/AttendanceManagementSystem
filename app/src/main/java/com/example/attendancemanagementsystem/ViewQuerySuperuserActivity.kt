package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.DropDownEmployeeAdapter
import com.example.attendancemanagementsystem.adapter.ViewQuriesSUAdapter
import com.example.employee.api.SuperUser
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewQuerySuperuserActivity : AppCompatActivity() {
    private lateinit var employeeSpinner: AutoCompleteTextView
    private lateinit var typeSpinner: AutoCompleteTextView
    private lateinit var hiddenId:TextView
    private lateinit var QueryGrid:GridView
    private lateinit var Dropdown: TextInputLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_query_superuser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Queries"

        typeSpinner=findViewById(R.id.AutoCompleteEmployeeTypeQuerySU)
        employeeSpinner=findViewById(R.id.AutoCompleteEmployeeQuerySU)
        hiddenId=findViewById(R.id.HiddenEmployeeIdSU)
        QueryGrid=findViewById(R.id.ViewQuerySuGrid)
        Dropdown=findViewById(R.id.EmployeeQuerySUDropdown)
        val Typearry = arrayListOf("Teaching","NonTeaching")
        val TypeAdapter= ArrayAdapter(this@ViewQuerySuperuserActivity ,android.R.layout.simple_list_item_1,Typearry)
        typeSpinner.setAdapter(TypeAdapter)
        val builder = AlertDialog.Builder(this@ViewQuerySuperuserActivity)

        var Type=""
        Dropdown.visibility=View.GONE
        typeSpinner.setOnItemClickListener { parent, view, position, id ->
            QueryGrid.visibility= View.GONE

            employeeSpinner.text.clear()
            Type = Typearry[position]
            CoroutineScope(Dispatchers.IO).launch {
                val employees = SuperUser.getAllEmployeeData(Type)
                withContext(Dispatchers.Main){
                    val employeeadapter= DropDownEmployeeAdapter(this@ViewQuerySuperuserActivity,employees)
                    employeeSpinner.setAdapter(employeeadapter)
                    var employeeId=""
                    Dropdown.visibility=View.VISIBLE
                    employeeSpinner.setOnItemClickListener { parent, view, position, id ->
                        employeeId=employees[position].employeeid.toString()
                        CoroutineScope(Dispatchers.IO).launch {
                            val Query = SuperUser.getAllQueries(employeeId)
                            withContext(Dispatchers.Main){
                                QueryGrid.visibility= View.GONE
                                if(Query.isEmpty()){
                                    //set title for alert dialog
                                    builder.setTitle("No Data!!")
                                    //set message for alert dialog
                                    builder.setMessage("No Data Found...")
                                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                                    //performing cancel action
                                    builder.setNeutralButton("Ok"){dialogInterface , which ->
                                    }
                                    // Create the AlertDialog
                                    val alertDialog: AlertDialog = builder.create()
                                    // Set other dialog properties
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }else{
                                    QueryGrid.visibility= View.VISIBLE

                                    QueryGrid.adapter=
                                            ViewQuriesSUAdapter(this@ViewQuerySuperuserActivity,Query)
                                }
                            }

                        }
                    }
                }
            }
        }


    }
}