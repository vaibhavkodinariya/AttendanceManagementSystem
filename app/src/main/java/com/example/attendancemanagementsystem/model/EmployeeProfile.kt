package com.example.attendancemanagementsystem.model

class EmployeeProfile(
    var employeeId:Int,
    var firstname:String,
    var middlename:String,
    var lastname:String,
    var type:String,
    var gender:String,
    var email:String,
    var phonenumber:String,
    var password:String,
    var flatno:String,
    var area:String,
    var city:String,
    var state:String,
    var pincode:Int,
    var subjects:Array<ListofSubjects>
)

class ListofSubjects(var subjectid:Int?,var subjects: String,var semesterid:String?){
    override fun toString(): String {
        return subjects
    }
}