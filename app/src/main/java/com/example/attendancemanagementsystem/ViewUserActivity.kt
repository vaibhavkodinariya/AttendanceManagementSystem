package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.adapter.SubjectAdapter
import com.example.employee.api.Employees
import com.example.employee.api.Students
import com.google.android.material.chip.ChipGroup
import com.google.android.material.divider.MaterialDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewUserActivity : AppCompatActivity() {
    private lateinit var userName : TextView
    private lateinit var Email : TextView
    private lateinit var Dob : TextView
    private lateinit var Address : TextView
    private lateinit var Enroll : TextView
    private lateinit var PhoneNo : TextView
    private lateinit var Subjects : TextView
    private lateinit var division : TextView
    private lateinit var EnrollDivider : MaterialDivider
    private lateinit var DobDivider : MaterialDivider
    private lateinit var ProgramTitle : TextView
    private lateinit var ProgramDivider : MaterialDivider
    private lateinit var EnrollLayout : View
    private lateinit var DobLayout : View
    private lateinit var ProgramLayout : View
    private lateinit var SubjectChip : ChipGroup
    private lateinit var SubjectTitle : TextView
    private lateinit var ChipGrid : GridView
    private lateinit var SubjectLayout: LinearLayout
    private lateinit var SubjectDivider: MaterialDivider

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="User Detail"

        userName = findViewById(R.id.UserNameText)
        Email = findViewById(R.id.EmailText)
        Enroll = findViewById(R.id.EnrollmentText)
        PhoneNo = findViewById(R.id.PhoneNumberText)
        Address = findViewById(R.id.AddressText)
        Dob = findViewById(R.id.DobText)
        Subjects = findViewById(R.id.SubjectsText)
        division = findViewById(R.id.ProgramText)
        EnrollLayout = findViewById(R.id.EnrollLayout)
        EnrollDivider = findViewById(R.id.DividerEnrollment)
        DobLayout = findViewById(R.id.DobLayout)
        DobDivider = findViewById(R.id.DividerDob)
        ProgramLayout = findViewById(R.id.ProgramsLayout)
        ProgramDivider = findViewById(R.id.DividerProgram)
        ProgramTitle = findViewById(R.id.ProgramTitle)
        SubjectChip = findViewById(R.id.SubjectsChip)
        SubjectTitle = findViewById(R.id.SubjectsTitle)
        SubjectLayout=findViewById(R.id.SubjectsLayout)
        SubjectDivider=findViewById(R.id.DividerSubjects)
        ChipGrid = findViewById(R.id.ChipGrid)

        val id=intent.getStringExtra("id")!!
        val type=intent.getStringExtra("type")!!
        if(type=="student"){
            CoroutineScope(Dispatchers.IO).launch {
                val profile= Students.getStudentProfile(id)
                withContext(Dispatchers.Main){
                    userName.text=profile.firstname+" "+profile.middlename+" "+profile.lastname
                    Email.text=profile.email
                    Enroll.text=id
                    PhoneNo.text=profile.phonenumber
                    Address.text=profile.flatno+" "+profile.area+" "+profile.city+" "+profile.state+" "+profile.pincode
                    Dob.text=profile.dob
                    Subjects.text=profile.semester
                    division.text=profile.division
                    SubjectChip.visibility= View.GONE
                    SubjectTitle.text = "Semester"
                    ProgramTitle.text = "Division"
                }
            }
        }else if(type!="student"){
            CoroutineScope(Dispatchers.IO).launch {
                val profile= Employees.getEmployeeProfile(id)
                withContext(Dispatchers.Main){
                    userName.text=profile.firstname+" "+profile.middlename+" "+profile.lastname
                    Email.text=profile.email
                    EnrollLayout.visibility= View.GONE
                    Enroll.visibility= View.GONE
                    EnrollDivider.visibility= View.GONE
                    PhoneNo.text=profile.phonenumber
                    Address.text=profile.flatno+" "+profile.area+" "+profile.city+" "+profile.state+" "+profile.pincode
                    Dob.visibility= View.GONE
                    DobDivider.visibility= View.GONE
                    DobLayout.visibility= View.GONE
                    Subjects.visibility= View.GONE
                    ProgramLayout.visibility= View.GONE
                    division.visibility= View.GONE
                    ProgramDivider.visibility= View.GONE

                    ChipGrid.isNestedScrollingEnabled = true
                    ChipGrid.adapter= SubjectAdapter(this@ViewUserActivity,profile.subjects)
                }
            }
        }
    }
}