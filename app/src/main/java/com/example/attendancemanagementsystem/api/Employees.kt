package com.example.employee.api

//import com.example.employee.model.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.model.*
import com.example.employee.api.Students.Companion.ipaddress
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Employees : AppCompatActivity() {
        companion object {
            fun getStudentByDivision(subject:String?,id:String?):Array<Student>{
                val studentArrayList = arrayListOf<Student>()
                val url = URL("http://$ipaddress/ams/employees/getStudentsByDivision/${subject}/${id}")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0)
                    }
                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readEmployee = httpConnection.inputStream.bufferedReader()
                        val jsonresEmployee = readEmployee.readText()
                        val employeeData = JSONObject(jsonresEmployee)
                        val EmployeeArray = employeeData.getJSONArray("students")
                        var i = 0
                        while (i < EmployeeArray.length()) {
                            val data = EmployeeArray.getJSONObject(i)
                            val employee = Student(
                                data.getString("enrollmentno"),
                                data.getString("firstname"),
                                data.getString("middlename"),
                                data.getString("lastname"),
                                null
                            )
                            studentArrayList.add(employee)
                            i++
                        }
                        return studentArrayList.toTypedArray()
                    }
                } catch (ex: Exception) {
                    Log.e("Error", ex.message!!)
                }
                return null!!
            }

            fun updateAttendance(subject:String?,employeeid: String?,date:String?,attendance: JSONArray):JSONObject{
                val jsonRes=JSONObject()
                jsonRes.put("subject",subject)
                jsonRes.put("employeeId",employeeid)
                jsonRes.put("date",date)
                jsonRes.put("attendance",attendance)
                val jsonData=jsonRes.toString()

                val url = URL("http://$ipaddress/ams/employees/updateAttendance")
                val httpconnection = (url.openConnection() as HttpURLConnection).apply {
                    doInput = true
                    doOutput = true
                    requestMethod = "PUT"
                    setRequestProperty("Content-Type", "application/json")
                }
                try {
                    val writeQuery = httpconnection.outputStream.bufferedWriter()
                    writeQuery.write(jsonData)
                    writeQuery.flush()
                    if (httpconnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val Queryreader = httpconnection.inputStream.bufferedReader()
                        val jsonResponseQuery = Queryreader.readText()
                        return JSONObject(jsonResponseQuery)
                    }

                } catch (ex: java.lang.Exception) {
                    Log.e("Error in send Query", ex.message!!)
                }
                return null!!
            }

            fun getAllEmployees(): Array<ListofEmployee> {
                val employeesArrayList = arrayListOf<ListofEmployee>()
                val url = URL("http://$ipaddress/ams/employees")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply { setChunkedStreamingMode(0) }

                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readEmployee = httpConnection.inputStream.bufferedReader()
                        val jsonresEmployee = readEmployee.readText()
                        val employeeData = JSONObject(jsonresEmployee)
                        val EmployeeArray = employeeData.getJSONArray("employees")
                        var i = 0
                        while (i < EmployeeArray.length()) {
                            val data = EmployeeArray.getJSONObject(i)
                            val employee = ListofEmployee(
                                data.getInt("employeeid"),
                                data.getString("firstname"),
                                data.getString("middlename"),
                                data.getString("lastname")
                            )
                            employeesArrayList.add(employee)
                            i++
                        }
                    return employeesArrayList.toTypedArray()
                    }
                } catch (ex: Exception) {
                    Log.e("Error", ex.message!!)
                }
                return null!!
            }

            fun getAllQueries(id: String?): Array<Queries> {
                val queryArrayList = arrayListOf<Queries>()
                val url = URL("http://${ipaddress}/ams/employees/queries/${id}")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply { setChunkedStreamingMode(0) }

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
                                data.getString("lastname"),
                                data.getString("middlename"),
                                null
                            )
                            queryArrayList.add(Query)
                            i++
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("Query Error", ex.message!!)
                }
                return queryArrayList.toTypedArray()
            }

            fun sendQuery(
                Description: String,
                EmployeeId: String,
                EnrollmentNo: String,
                SubjectId: String,
                Semester: String,
                data:String
            ): JSONObject {
                val jsonQuery = JSONObject()
                jsonQuery.put("description", Description)
                jsonQuery.put("employeeId", EmployeeId)
                jsonQuery.put("enrollmentNumber", EnrollmentNo)
                jsonQuery.put("programId", 1)
                jsonQuery.put("subjectId", SubjectId)
                jsonQuery.put("semesterId", Semester)

                val jsonQueryResponse = jsonQuery.toString()

                val url = URL("http://$ipaddress/ams/${data}/queries")
                val httpconnection = (url.openConnection() as HttpURLConnection).apply {
                    doInput = true
                    doOutput = true
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                }
                try {
                    val writeQuery = httpconnection.outputStream.bufferedWriter()
                    writeQuery.write(jsonQueryResponse)
                    writeQuery.flush()
                    if (httpconnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val Queryreader = httpconnection.inputStream.bufferedReader()
                        val jsonResponseQuery = Queryreader.readText()
                        return JSONObject(jsonResponseQuery)
                    }

                } catch (ex: java.lang.Exception) {
                    Log.e("Error in send Query", ex.message!!)
                }
                    return null!!
            }

            fun getEmployeeProfile(id:String): EmployeeProfile {
                val url = URL("http://$ipaddress/ams/employees/getEmployeeProfile/${id}")
                val httpConnection = (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0) }

                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readEmployeeProfile = httpConnection.inputStream.bufferedReader()
                        val jsonEmployeeProfile = readEmployeeProfile.readText()
                        val employeeProfile = JSONObject(jsonEmployeeProfile)
                        val employeeProfileArray = employeeProfile.getJSONObject("employee")
                        val subjects = ArrayList<ListofSubjects>()
                        val subjectArray = employeeProfile.getJSONArray("subjects")
                        var i = 0
                        while (i < subjectArray.length()) {
                            var arrayList = ListofSubjects(
                                null,
                                subjectArray.getString(i),
                                null
                            )
                            subjects.add(arrayList)
                            i++
                        }

                        return EmployeeProfile(
                            employeeProfileArray.getInt("employeeid"),
                            employeeProfileArray.getString("firstname"),
                            employeeProfileArray.getString("middlename"),
                            employeeProfileArray.getString("lastname"),
                            employeeProfileArray.getString("type"),
                            employeeProfileArray.getString("gender"),
                            employeeProfileArray.getString("email"),
                            employeeProfileArray.getString("phone"),
                            employeeProfileArray.getString("password"),
                            employeeProfileArray.getString("flatno"),
                            employeeProfileArray.getString("area"),
                            employeeProfileArray.getString("city"),
                            employeeProfileArray.getString("state"),
                            employeeProfileArray.getInt("pincode"),
                            subjects.toTypedArray()
                        )
                    }
                } catch (ex: java.lang.Exception) {
                    Log.e("Profile error", ex.message!!)
                }
                return null!!
            }
            fun getEmployeeSubjects(id:String): Array<ListofSubjects> {
                val arrayofSubjects= arrayListOf<ListofSubjects>()
                val url = URL("http://$ipaddress/ams/employees/getEmployeesSubjects/${id}")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0) }

                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readEmployeeProfile = httpConnection.inputStream.bufferedReader()
                        val jsonEmployeeProfile = readEmployeeProfile.readText()
                        val employeeProfile = JSONObject(jsonEmployeeProfile)
                        val subjectArray = employeeProfile.getJSONArray("subjects")
                        var i = 0
                        while (i < subjectArray.length()) {
                            var arrayList = ListofSubjects(
                                null,
                                subjectArray.getString(i),
                                null
                            )
                            arrayofSubjects.add(arrayList)
                            i++
                        }
                        return arrayofSubjects.toTypedArray()
                    }
                } catch (ex: java.lang.Exception) {
                    Log.e("Subjects error", ex.message!!)
                }
                return null!!
            }

            fun getAllSubjects(): Array<ListofSubjects> {
                val arrayofSubjects= arrayListOf<ListofSubjects>()
                val url = URL("http://$ipaddress/ams/admin/subjects/getSubjects")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0) }

                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readEmployeeProfile = httpConnection.inputStream.bufferedReader()
                        val jsonEmployeeProfile = readEmployeeProfile.readText()
                        val employeeProfile = JSONObject(jsonEmployeeProfile)
                        val subjectArray = employeeProfile.getJSONArray("subjects")
                        var i = 0
                        while (i < subjectArray.length()) {
                            var data=subjectArray.getJSONObject(i)
                            var arrayList = ListofSubjects(
                                data.getInt("subjectid"),
                                data.getString("subjectname"),
                                null
                            )
                            arrayofSubjects.add(arrayList)
                            i++
                        }
                        return arrayofSubjects.toTypedArray()
                    }
                } catch (ex: java.lang.Exception) {
                    Log.e("Subjects error", ex.message!!)
                }
                return null!!
            }
            fun takeAttendance(
                Subject : String?,
                Date : String?,
                EmployeeId : String,
                Attendance : JSONObject
            ): JSONObject {
                val jsondata=JSONObject()
                jsondata.put("subject",Subject)
                jsondata.put("date",Date)
                jsondata.put("employeeid",EmployeeId)
                jsondata.put("attendance",Attendance)

                val jsonres =jsondata.toString()
                val url = URL("http://$ipaddress/ams/employees/takeAttendance")
                val httpConnection = (url.openConnection()as HttpURLConnection).apply {
                    doInput=true
                    doOutput=true
                    requestMethod="POST"
                    setRequestProperty("Content-Type","application/json")
                }
                try {
                    val writer=httpConnection.outputStream.bufferedWriter()
                    writer.write(jsonres)
                    writer.flush()
                    if(httpConnection.responseCode == HttpURLConnection.HTTP_OK){
                        val reader=httpConnection.inputStream.bufferedReader()
                        val responsejson = reader.readText()
                        return JSONObject(responsejson)
                    }

                }catch (ex : Exception){
                    Log.e("Error",ex.message!!)

                }
                return null!!
            }
            fun getAttendanceByDate(id: String?, date: String?, subject: String?):Array<Student> {
                var studentDetails = arrayListOf<Student>()
                val url =
                    URL("http://${ipaddress}/ams/employees/attendancedates/${id}/${date}/${subject}")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0)
                    }
                try {
                    if (httpConnection.responseCode==HttpURLConnection.HTTP_OK){
                        val readSubjectAttendance=httpConnection.inputStream.bufferedReader()
                        val jsonSubjectAttendance = readSubjectAttendance.readText()
                        val subjectAttendance = JSONObject(jsonSubjectAttendance)
                        val subjectAttendanceObject = subjectAttendance.getJSONArray("attendance")
                        var i=0
                        while (i< subjectAttendanceObject.length()){
                            val data=subjectAttendanceObject.getJSONObject(i)
                            val details=Student(
                                data.getString("enrollmentno"),
                                data.getString("firstname"),
                                data.getString("middlename"),
                                data.getString("lastname"),
                                data.getInt(date)
                            )
                            studentDetails.add(details)
                            i++
                        }
                        return studentDetails.toTypedArray()
                    }
                }catch (ex : java.lang.Exception){
                    Log.e("Get Attendance",ex.message!!)
                }
                return null!!

            }
            fun getAllTakenAttendance(employeeid:String?,subject:String):JSONObject{
                val url =
                    URL("http://$ipaddress/ams/employees/attendance/${employeeid}/${subject}")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0)
                    }

                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readTakenAttendance = httpConnection.inputStream.bufferedReader()
                        val jsonTakenAttendance = readTakenAttendance.readText()
                        val TakenAttendance = JSONObject(jsonTakenAttendance)
                        return TakenAttendance
                    }
                }catch (ex:Exception){
                    Log.e("Error",ex.message!!)
                }
                return null!!
            }

            fun getStudentAttendance(id:String,Subject : String?,EnrollmentNo: String?): JSONObject {
                val url = URL("http://${ipaddress}/ams/employees/getStudentAttendance/${id}/${Subject}/${EnrollmentNo}")
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
            fun getEmployeesBySubject(subjectid:String): Array<Employee> {
                var arrayListEmployee= arrayListOf<Employee>()
                val url = URL("http://${ipaddress}/ams/students/getEmployeesBySubjects/${subjectid}")
                val httpConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        doInput=true
                        requestMethod="GET"
                        setChunkedStreamingMode(0)
                    }

                try {
                    if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val readStudentAttendance = httpConnection.inputStream.bufferedReader()
                        val jsonStudentAttendance = readStudentAttendance.readText()
                        val StudentAttendance = JSONObject(jsonStudentAttendance)
                        val StudentAttendanceArray = StudentAttendance.getJSONArray("employees")

                        var i = 0
                        while (i < StudentAttendanceArray.length()) {
                            var getEmployee=StudentAttendanceArray.getJSONObject(i)
                            var employees=Employee(
                            getEmployee.getInt("employeeid"),
                            getEmployee.getString("firstname"),
                            getEmployee.getString("middlename"),
                            getEmployee.getString("lastname"),
                            null,
                            null,
                                null
                            )
                            arrayListEmployee.add(employees)
                            i++
                        }
                        return arrayListEmployee.toTypedArray()
                    }
                } catch (ex: java.lang.Exception) {
                    Log.e("Total Attendance error", ex.message!!)
                }
                return null!!
            }
        }
}