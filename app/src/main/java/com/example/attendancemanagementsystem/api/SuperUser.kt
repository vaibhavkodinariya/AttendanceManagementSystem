package com.example.employee.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.model.Employee
import com.example.attendancemanagementsystem.model.Queries
import com.example.attendancemanagementsystem.model.Student
import com.example.employee.api.Students.Companion.ipaddress
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SuperUser : AppCompatActivity() {
    companion object {
        fun getAllStudentData(Division:String,Semester:String): Array<Student> {
            val studentArrayList = arrayListOf<Student>()
            val url = URL("http://${ipaddress}/ams/superAdmin/getStudents/${Division}/${Semester}")

            val httpConnection =
                (url.openConnection() as HttpURLConnection).apply { setChunkedStreamingMode(0) }

            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val readStudent = httpConnection.inputStream.bufferedReader()
                    val jsonresStudent = readStudent.readText()
                    val studentData = JSONObject(jsonresStudent)
                    val studentArray = studentData.getJSONArray("students")

                    var i = 0
                    while (i < studentArray.length()) {
                        val data = studentArray.getJSONObject(i)
                        val student = Student(
                            data.getString("enrollmentno"),
                            data.getString("firstname"),
                            data.getString("middlename"),
                            data.getString("lastname"),
                            null
                        )
                        studentArrayList.add(student)
                        i++
                        Log.i("Data",studentArrayList.toString())
                    }
                }
            } catch (ex: Exception) {
                Log.e("Student Data", ex.message!!)
            }
            return studentArrayList.toTypedArray()
        }

        fun getAllEmployeeData(type:String): Array<Employee> {
            val employeesArrayList = arrayListOf<Employee>()
            val url = URL("http://${ipaddress}/ams/superAdmin/getEmployees/${type}")
            val httpConnection =
                (url.openConnection() as HttpURLConnection).apply {
                    doInput=true
                    requestMethod="GET"
                    setChunkedStreamingMode(0) }

            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val readEmployee = httpConnection.inputStream.bufferedReader()
                    val jsonresEmployee = readEmployee.readText()
                    val employeeData = JSONObject(jsonresEmployee)
                    val EmployeeArray = employeeData.getJSONArray("employees")
                    var i = 0
                    while (i < EmployeeArray.length()) {
                        val data = EmployeeArray.getJSONObject(i)
                        val employee = Employee(
                            data.getInt("employeeid"),
                            data.getString("firstname"),
                            data.getString("middlename"),
                            data.getString("lastname"),
                            null,
                            data.getString("email"),
                            null
                        )
                        employeesArrayList.add(employee)
                        i++
                    }

                }
            } catch (ex: Exception) {
                Log.e("Error", ex.message!!)
            }
            return employeesArrayList.toTypedArray()
        }

        fun getAllQueries(employeeId : String): Array<Queries> {
            val queryArrayList = arrayListOf<Queries>()
            val url = URL("http://${ipaddress}/ams/superAdmin/queries/${employeeId}")
            val httpConnection =
                (url.openConnection() as HttpURLConnection).apply {
                    doInput=true
                    requestMethod="GET"
                    setChunkedStreamingMode(0) }

            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val readQueries = httpConnection.inputStream.bufferedReader()
                    val jsonresQuery = readQueries.readText()
                    val queryData = JSONObject(jsonresQuery)
                    val queryArray = queryData.getJSONArray("queries")

                    var i = 0
                    while (i < queryArray.length()) {
                        val data = queryArray.getJSONObject(i)
                        val Query = Queries(
                            null,
                            data.getString("subjectname"),
                            data.getString("description"),
                            data.getString("firstname"),
                            null,
                            data.getString("middlename"),
                            data.getString("enrollmentno")
                        )
                        queryArrayList.add(Query)
                        i++
                    }
                    Log.i("Data",queryArrayList.toString())
                }
            } catch (ex: Exception) {
                Log.e("Query Error", ex.message!!)
            }
            return queryArrayList.toTypedArray()
        }


        fun DeleteData(Id : String, Type : String) : JSONObject{
            val datajson=JSONObject()
            datajson.put("ID",Id)
            datajson.put("Type",Type)

            val jsonResponse=datajson.toString()
            val url = URL("http://${ipaddress}/ams/superAdmin/deleteData/${Id}/${Type}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput=true
                doOutput=true
                requestMethod="DELETE"
                setRequestProperty("Content-Type","application/json")
                setChunkedStreamingMode(0)
            }
            try {
                val writeJson = httpConnection.outputStream.bufferedWriter()
                writeJson.write(jsonResponse)
                writeJson.flush()
                if (httpConnection.responseCode==HttpURLConnection.HTTP_OK){
                    val readJson=httpConnection.inputStream.bufferedReader()
                    val readJsonResponse = readJson.readText()
                    return JSONObject(readJsonResponse)
                }

            }catch (ex : Exception){
                Log.e("Error",ex.message!!)
            }
            return null!!
        }
    }
}