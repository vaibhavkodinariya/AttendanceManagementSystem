package com.example.employee.api

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.model.DailyAttendance
import com.example.attendancemanagementsystem.model.ListofSubjects
import com.example.attendancemanagementsystem.model.Student
import com.example.attendancemanagementsystem.model.StudentProfile
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Students : AppCompatActivity() {
    companion object{
        val ipaddress = "" // enter your network IP
        fun login(Data : String, Password : String, Type : String) : JSONObject{
            val jsonLogin = JSONObject()
            jsonLogin.put("data",Data)
            jsonLogin.put("password",Password)
            jsonLogin.put("type",Type)

            val jsonLoginResponse = jsonLogin.toString()

            val url = URL("http://${ipaddress}/ams/login")
            val httpconnection = (url.openConnection() as HttpURLConnection).apply {
                doInput=true
                doOutput=true
                requestMethod="POST"
                setRequestProperty("Content-Type","application/json")
            }
            try {
                val writeLogindata= httpconnection.outputStream.bufferedWriter()
                writeLogindata.write(jsonLoginResponse)
                writeLogindata.flush()
                if (httpconnection.responseCode==HttpURLConnection.HTTP_OK){
                    val jsonResponse = httpconnection.inputStream.bufferedReader()
                    val jsonResponseLogin=jsonResponse.readText()
                    return JSONObject(jsonResponseLogin)
                }
            }catch (ex : Exception){
                Log.e("Login Error",ex.message!!)
            }
            return null!!

        }
        fun getStudentProfile(id:String?) : StudentProfile {
            val url = URL("http://${ipaddress}/ams/students/getStudent/${id}")
            val httpconnection = (url.openConnection() as HttpURLConnection).apply {
                doInput=true
                setChunkedStreamingMode(0)
            }
            try {
                if (httpconnection.responseCode==HttpURLConnection.HTTP_OK){
                    val readStudentProfile = httpconnection.inputStream.bufferedReader()
                    val jsonStudentProfile = readStudentProfile.readText()
                    val studentProfile = JSONObject(jsonStudentProfile)
                    val studentProfileArray = studentProfile.getJSONObject("students")

                    return StudentProfile(
                        studentProfileArray.getString("enrollmentno"),
                        studentProfileArray.getString("firstname"),
                        studentProfileArray.getString("middlename"),
                        studentProfileArray.getString("lastname"),
                        studentProfileArray.getString("dob"),
                        studentProfileArray.getString("gender"),
                        studentProfileArray.getString("email"),
                        studentProfileArray.getString("phone"),
                        studentProfileArray.getString("password"),
                        studentProfileArray.getString("flatno"),
                        studentProfileArray.getString("area"),
                        studentProfileArray.getString("city"),
                        studentProfileArray.getString("state"),
                        studentProfileArray.getInt("pincode"),
                        studentProfileArray.getString("division"),
                        studentProfileArray.getString("semester"),
                        studentProfileArray.getInt("programid")
                    )
                }
            }catch (ex : Exception){
                Log.e("Login Error",ex.message!!)
            }
            return null!!
        }
        fun getQueries(id:String) : JSONObject {
            val url =URL("http://${ipaddress}/ams/students/queries/${id}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput=true
                requestMethod="GET"
                setChunkedStreamingMode(0) }

            try {
                if (httpConnection.responseCode==HttpURLConnection.HTTP_OK){
                    val readStudent = httpConnection.inputStream.bufferedReader()
                    val jsonresStudent = readStudent.readText()
                    val studentData = JSONObject(jsonresStudent)
                    return studentData
                }
            }catch (ex : Exception)
            {
                Log.e("Student Data", ex.message!!)
            }
            return null!!
        }
        fun getDailyAttendance(enrollmentno:String?): Array<DailyAttendance> {
            val url = URL("http://${ipaddress}/ams/students/getDailyAttendance/${enrollmentno}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                requestMethod = "GET"
                setChunkedStreamingMode(0)
            }
            val attendanceList = mutableListOf<DailyAttendance>()
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val readDailyAttendance = httpConnection.inputStream.bufferedReader()
                    val jsonDailyAttendance = readDailyAttendance.readText()
                    val dailyAttendance = JSONObject(jsonDailyAttendance)
                    val dailyAttendanceArray = dailyAttendance.getJSONArray("dailyAttendance")
                    for (i in 0 until dailyAttendanceArray.length()) {
                        val jsonObject = dailyAttendanceArray.getJSONObject(i)
                        val keys = jsonObject.keys()

                        while (keys.hasNext()) {
                            val key = keys.next()
                            val platformData = jsonObject.optJSONObject(key)

                            if (platformData != null) {
                                val platformDataKeys = platformData.keys()
                                while (platformDataKeys.hasNext()) {
                                    val dateTime = platformDataKeys.next()
                                    val attendance = platformData.getInt(dateTime)
                                    val (date, timeSlot) = dateTime.split("L")
                                    val attendanceObj = DailyAttendance(key, date, timeSlot, attendance)
                                    attendanceList.add(attendanceObj)
                                }
                            }
                        }
                    }
                    return attendanceList.toTypedArray()
                }
            } catch (ex: Exception) {
                Log.e("Error", ex.message!!)
            }
            return null!!
        }
        fun getAttendance(Subject : String?,EnrollmentNo: String?,data:String?): JSONObject {
            val url = URL("http://${ipaddress}/ams/${data}/attend/${Subject}/${EnrollmentNo}")
            val httpConnection =
                (url.openConnection() as HttpURLConnection).apply {
                    doInput=true
                    setChunkedStreamingMode(0)
                }
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val readStudentAttendance = httpConnection.inputStream.bufferedReader()
                    val jsonStudentAttendance = readStudentAttendance.readText()
                    val StudentAttendance = JSONObject(jsonStudentAttendance)
                    return StudentAttendance
                }
            } catch (ex: java.lang.Exception) {
                Log.e("Total Attendance error", ex.message!!)
            }
            return null!!
        }

        fun getSubjectsByStudent(id:String) : Array<ListofSubjects>{
            val subjects = arrayListOf<ListofSubjects>()
            val url =URL("http://${ipaddress}/ams/students/getSubject/${id}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput=true
                requestMethod="GET"
                setChunkedStreamingMode(0) }

            try {
                if (httpConnection.responseCode==HttpURLConnection.HTTP_OK){
                    val readStudent = httpConnection.inputStream.bufferedReader()
                    val jsonresStudent = readStudent.readText()
                    val studentData = JSONObject(jsonresStudent)
                    val studentArray = studentData.getJSONArray("subjects")
                    var i = 0
                    while (i < studentArray.length()){
                        val data = studentArray.getJSONObject(i)
                        val studentQueries = ListofSubjects(
                            data.getInt("subjectid"),
                            data.getString("subjectname"),
                            data.getString("semesterid")
                        )
                        subjects.add(studentQueries)
                        i++
                    }
                    return subjects.toTypedArray()
                }
            }catch (ex : Exception)
            {
                Log.e("Student Data", ex.message!!)
            }
            return null!!
        }
        fun getAllStudentData() : ArrayList<Student>{
            val studentArrayList = arrayListOf<Student>()
            val url =URL("http://${Students.ipaddress}/ams/students")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply { setChunkedStreamingMode(0) }

            try {
                if (httpConnection.responseCode==HttpURLConnection.HTTP_OK){
                    val readStudent = httpConnection.inputStream.bufferedReader()
                    val jsonresStudent = readStudent.readText()
                    val studentData = JSONObject(jsonresStudent)
                    val studentArray = studentData.getJSONArray("students")
                    var i = 0
                    while (i < studentArray.length()){
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
                    }
                }
            }catch (ex : Exception)
            {
                Log.e("Student Data", ex.message!!)
            }
            return studentArrayList
        }
        fun getStudentAttendanceByMonth(id: String?,subject:String?,month:String?) :JSONObject {
            val url =
                URL("http://${ipaddress}/ams/students/getStudentAttendanceByMonth/${id}/${subject}/${month}")
            val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                doInput = true
                requestMethod = "GET"
                setChunkedStreamingMode(0)
            }
            try {
                if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val readMonthlyAttendance = httpConnection.inputStream.bufferedReader()
                    val jsonMonthlyAttendance = readMonthlyAttendance.readText()
                    val MonthlyAttendance = JSONObject(jsonMonthlyAttendance)
                    return MonthlyAttendance
                }

            }catch (ex : Exception){
                Log.e("Error",ex.message!!)

            }
            return  null!!
        }
    }
}
