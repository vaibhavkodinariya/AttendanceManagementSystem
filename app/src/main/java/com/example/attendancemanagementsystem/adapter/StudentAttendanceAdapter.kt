package com.example.attendancemanagementsystem.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.attendancemanagementsystem.HomeActivity
import com.example.attendancemanagementsystem.R
import com.example.attendancemanagementsystem.model.Student
import com.example.employee.api.Employees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class StudentAttendanceAdapter(private val activity: Activity,private val students : Array<Student>,
                               private var updateBtn:Button,private val date:String?,
                               private val subject:String?) : ArrayAdapter<Student>(activity,
    R.layout.takeattendancelayout,students) {

    private val Attendance = mutableMapOf<String?,Int>()
    var atndn=0
    val sharePreference = activity.getSharedPreferences("Credentials", AppCompatActivity.MODE_PRIVATE)
    val employeeId=sharePreference.getString("id","")
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view :View?= convertView
        val viewHolder:ViewHolder
        if (view==null){
            view=activity.layoutInflater.inflate(R.layout.takeattendancelayout,parent,false)
            viewHolder= ViewHolder()

            viewHolder.EnrollmentNo=view.findViewById(R.id.Enrollment)
            viewHolder.StudentName=view.findViewById(R.id.StudentName)
            viewHolder.Checked=view.findViewById(R.id.Attendance)
            viewHolder.cardAtt = view.findViewById(R.id.CardAttendance1)
            view.tag=viewHolder
        }else
            viewHolder=view.tag as ViewHolder

        viewHolder.EnrollmentNo.text=students[position].EnrollmentNo
        viewHolder.StudentName.text=students[position].FirstName+students[position].MiddleName+students[position].LastName
        viewHolder.cardAtt.setOnClickListener {
            if (viewHolder.Checked.isChecked)
            {
                viewHolder.Checked.isChecked=false
                viewHolder.cardAtt.setBackgroundResource(R.color.red)
            }
            else
            {
                viewHolder.Checked.isChecked=true
                viewHolder.cardAtt.setBackgroundResource(R.color.green)
            }
        }
        if(students[position].isPresent == 1){
            viewHolder.Checked.isChecked=true
            viewHolder.cardAtt.setBackgroundResource(R.color.green)

        }else{
            viewHolder.Checked.isChecked=false
            viewHolder.cardAtt.setBackgroundResource(R.color.red)
        }


        viewHolder.Checked.setOnCheckedChangeListener { compoundButton, b ->
            if (viewHolder.Checked.isChecked) {
                viewHolder.Checked.setChecked(true)
                viewHolder.cardAtt.setBackgroundResource(R.color.green)
                atndn = 1
                Attendance.put(students[position].EnrollmentNo, atndn)

            }
            else {
                atndn = 0
                viewHolder.cardAtt.setBackgroundResource(R.color.red)
                Attendance.put(students[position].EnrollmentNo, atndn)
                Toast.makeText(
                    activity,
                    students[position].EnrollmentNo + atndn.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        updateBtn.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            //set title for alert dialog
            builder.setTitle("Attention !")
            //set message for alert dialog
            builder.setMessage("Do you Want to Update Attendance ..!")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing cancel action
            builder.setPositiveButton("Yes"){dialogInterface , which ->
                val jsonArray = JSONArray()
                Attendance.forEach { (enrollmentNo, attend) ->
                    val jsonObject = JSONObject()
                    jsonObject.put("enrollmentno", enrollmentNo?.toLong())
                    jsonObject.put("attend", attend)
                    jsonArray.put(jsonObject)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val messege=Employees.updateAttendance(subject,employeeId,date,jsonArray)
                    withContext(Dispatchers.Main){
                        if(messege.getBoolean("success")){
                            Toast.makeText(activity, messege.getString("messege"), Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity, messege.getString("messege"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val intent = Intent(activity, HomeActivity::class.java)
                activity.startActivity(intent)
            }

            builder.setNegativeButton("No"){dialogInterface,which->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }
        return view!!
    }
    companion object {
        class ViewHolder {
            lateinit var EnrollmentNo : TextView
            lateinit var StudentName:TextView
            lateinit var Checked :CheckBox
            lateinit var cardAtt :CardView
        }
    }
}