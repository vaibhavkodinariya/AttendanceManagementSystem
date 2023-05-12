package com.example.attendancemanagementsystem

import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.EmployeeQueriesAdapter
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmloyeeQueryActivity : AppCompatActivity() {
    private lateinit var query: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emloyee_query)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Queries"
        query = findViewById(R.id.ViewQueryEmployeeGridView)

        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val userId = sharePreference.getString("id", "")!!
        CoroutineScope(Dispatchers.IO).launch {
            var queries = Employees.getAllQueries(userId)
            withContext(Dispatchers.Main) {
                if (queries.isEmpty()){

                }else{

                    query.adapter = EmployeeQueriesAdapter(this@EmloyeeQueryActivity, queries)
            }
            }
        }
    }
}