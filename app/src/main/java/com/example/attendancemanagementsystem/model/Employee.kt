package com.example.attendancemanagementsystem.model

class Employee(
    var employeeid:Int,
    var firstname:String,
    var middlename:String,
    var lastname:String,
    var gender:String?,
    var email:String?,
    var type : String?
){
    override fun toString(): String {
        return firstname+" "+middlename+" "+lastname
    }
}

class ListofEmployee(
    var id:Int,
    var firstname: String,
    var middlename:String,
    var lastname:String
    ){
    override fun toString(): String {
        return firstname+" "+middlename+" "+lastname
    }
}