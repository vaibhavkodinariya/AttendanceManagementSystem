package com.example.attendancemanagementsystem.model

class Student(
    var EnrollmentNo: String?,
    var FirstName: String,
    var MiddleName: String,
    var LastName: String,
    var isPresent:Int?,
    var isChecked:Boolean=false!!
)