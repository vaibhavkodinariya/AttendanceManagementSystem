package com.example.attendancemanagementsystem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.employee.api.Employees
import com.example.employee.api.Students
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifImageView
import kotlin.system.exitProcess


class HomeActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var cardTopLeftImg: ImageView
    private lateinit var cardTopRightImg: ImageView
    private lateinit var cardBottomRightImg: ImageView
    private lateinit var cardBottomLeftImg: ImageView
    private lateinit var cardTopLeftText: TextView
    private lateinit var cardTopRightText: TextView
    private lateinit var cardBottomRightText: TextView
    private lateinit var cardBottomLeftText: TextView
    private lateinit var cardTopLeft: CardView
    private lateinit var cardTopRight: CardView
    private lateinit var cardBottomRight: CardView
    private lateinit var cardBottomLeft: CardView
    private lateinit var type : String
    private lateinit var layout:ConstraintLayout
    private lateinit var userIdText : TextView
    private lateinit var userNameText : TextView
    private lateinit var headerUserIdText : TextView
    private lateinit var headerUserNameText : TextView
    private lateinit var circularGif: CircularRevealCardView
    private lateinit var CurveRactangle:ImageView
    private lateinit var GenderGif: GifImageView
    private lateinit var NavGender: GifImageView
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val id=sharePreference.getString("id","")!!
        if (id == ""){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
            exitProcess(0)
        }

        type=sharePreference.getString("type","")!!

        setContentView(R.layout.activity_home)
//        supportActionBar?.title=""
        navigationView = findViewById(R.id.NavView)
        val header = navigationView.getHeaderView(0)
        drawer = findViewById(R.id.navDrawer)
        cardTopLeftImg = findViewById(R.id.CardTopLeftImg)
        cardTopLeftText = findViewById(R.id.CardTopLeftText)
        cardTopRightImg = findViewById(R.id.CardTopRightImg)
        cardTopRightText = findViewById(R.id.CardTopRightText)
        cardBottomLeftImg = findViewById(R.id.CardBottomLeftImg)
        cardBottomLeftText = findViewById(R.id.CardBottomLeftText)
        cardBottomRightImg = findViewById(R.id.CardBottomRightImg)
        cardBottomRightText = findViewById(R.id.CardBottomRightText)
        cardTopRight = findViewById(R.id.CardTopRight)
        cardTopLeft = findViewById(R.id.CardTopLeft)
        cardBottomLeft = findViewById(R.id.CardBottomLeft)
        cardBottomRight = findViewById(R.id.CardBottomRight)
        userIdText = findViewById(R.id.UserIdText)
        userNameText = findViewById(R.id.UserNameText)
        headerUserIdText = header.findViewById(R.id.Userid)
        headerUserNameText = header.findViewById(R.id.Username)
        circularGif=findViewById(R.id.CircleCardTeacher)
        GenderGif=findViewById(R.id.genderGif)
        CurveRactangle=findViewById(R.id.rectangleCurve)
        NavGender=header.findViewById(R.id.malegif)
        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        layout=findViewById(androidx.constraintlayout.widget.R.id.layout)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cardTopLeft.setOnClickListener{

            if(type=="Teaching")
            {
                val selectDetailsTakeAttendanceintent =
                    Intent(this, SelectDetailsTakeAttendance::class.java)
                startActivity(selectDetailsTakeAttendanceintent)
            }
            else if(type=="student"){
                val DailyAttendance =
                    Intent(this, DailyAttendanceActivity::class.java)
                startActivity(DailyAttendance)
            }
            else if(type=="superAdmin"){
                val DeleteUser =
                    Intent(this, DeleteUserActivity::class.java)
                startActivity(DeleteUser)
            }
        }
        cardTopRight.setOnClickListener {
            if (type=="Teaching")
            {
                val query =
                    Intent(this, EmloyeeQueryActivity::class.java)
                startActivity(query)
            }
            else if(type=="student"){
                val monthly =
                    Intent(this, MonthlyAttendanceActivity::class.java)
                startActivity(monthly)
            }
            else if(type=="superAdmin"){
                val query =
                    Intent(this, ViewQuerySuperuserActivity::class.java)
                startActivity(query)
            }
        }
        cardBottomRight.setOnClickListener {
            if (type!="student")
            {
                val UpdateAttendance =
                    Intent(this, UpdateAttendanceDateEmployee::class.java)
                startActivity(UpdateAttendance)
            }
            else{
                val SubjectWise =
                    Intent(this, SubjectWiseAttendanceActivity::class.java)
                startActivity(SubjectWise)
            }
        }
        cardBottomLeft.setOnClickListener {
            if (type=="Teaching")
            {
                val ViewAttendanceEmploye =
                    Intent(this, SelectDetailsViewAttendanceActivity::class.java)
                startActivity(ViewAttendanceEmploye)
            }
            else if(type=="student"){
                val StudentQuery =
                    Intent(this, StudentQueryActivity::class.java)
                startActivity(StudentQuery)
            }
            else if(type=="superAdmin"){
                val ViewAttSU=
                    Intent(this, ViewAttendanceSuperuserActivity::class.java)
                startActivity(ViewAttSU)
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.menu_profile -> {
                    val profileintent = Intent(this, ProfileActivity::class.java)
                    startActivity(profileintent)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.menu_logout -> {
                    val preferences = getSharedPreferences("Credentials", 0)
                    preferences.edit().remove("id").apply()
                    preferences.edit().remove("type").apply()
                    Toast.makeText(this, "Logged Out SuccessFully", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    CoroutineScope(Dispatchers.IO).launch {
        if(isOnline(this@HomeActivity))
        {
            if(type=="student"){
                CoroutineScope(Dispatchers.IO).launch {
                    var profile = Students.getStudentProfile(id)
                    withContext(Dispatchers.Main) {
                        if (type == "student") {
                            cardTopLeftImg.setBackgroundResource(R.drawable.checkedusermale)
                            cardTopRightImg.setBackgroundResource(R.drawable.calendar)
                            cardTopRightText.text = "Monthly Attendance"
                            cardTopLeftText.text = "Daily Attendance"
                            cardBottomLeftImg.setBackgroundResource(R.drawable.faq)
                            cardBottomRightImg.setBackgroundResource(R.drawable.todolist)
                            cardBottomRightText.text = "Subject Wise Attendance"
                            cardBottomLeftText.text = "Query!"
                            circularGif.visibility= View.VISIBLE
                            CurveRactangle.visibility= View.VISIBLE
                            userIdText.text = id
                            userNameText.text =
                                profile.firstname + " " + profile.middlename + " " + profile.lastname
                            headerUserIdText.text = id
                            headerUserNameText.text =
                                profile.firstname + " " + profile.middlename + " " + profile.lastname
                            if(profile.gender.equals("male")){
                                GenderGif.setBackgroundResource(R.drawable.male1)
                                NavGender.setBackgroundResource(R.drawable.male1)
                            }else if(profile.gender.equals("female")){
                                GenderGif.setBackgroundResource(R.drawable.female1)
                                NavGender.setBackgroundResource(R.drawable.female1)

                            }
                            cardTopRight.visibility=View.VISIBLE
                            circularGif.visibility=View.VISIBLE
                            CurveRactangle.visibility=View.VISIBLE
                            cardTopLeft.visibility=View.VISIBLE
                            cardBottomLeft.visibility=View.VISIBLE
                            cardBottomRight.visibility=View.VISIBLE
                            layout.visibility=View.VISIBLE
                            navigationView.visibility=View.VISIBLE
                        }
                    }
                }
            }else if(type=="Teaching"){
                CoroutineScope(Dispatchers.IO).launch {
                    var profile=Employees.getEmployeeProfile(id)
                    withContext(Dispatchers.Main) {
                        cardTopLeftImg.setBackgroundResource(R.drawable.checkedusermale)
                        cardTopRightImg.setBackgroundResource(R.drawable.faq)
                        cardTopRightText.text="Query!"
                        cardTopLeftText.text="Take Attendance"
                        cardBottomLeftImg.setBackgroundResource(R.drawable.view)
                        cardBottomRightImg.setBackgroundResource(R.drawable.editproperty)
                        cardBottomRightText.text="Update Attendance"
                        cardBottomLeftText.text="View Attendance"
                        circularGif.visibility= View.VISIBLE
                        CurveRactangle.visibility= View.VISIBLE
                        userIdText.text=profile.email
                        if(profile.gender.equals("male")){
                            GenderGif.setBackgroundResource(R.drawable.male1)
                            NavGender.setBackgroundResource(R.drawable.male1)

                        }else if(profile.gender.equals("female")){
                            GenderGif.setBackgroundResource(R.drawable.female1)
                            NavGender.setBackgroundResource(R.drawable.female1)

                        }
                        userNameText.text= profile.firstname+" "+profile.middlename+" "+profile.lastname
                        headerUserIdText.text=profile.email
                        headerUserNameText.text= profile.firstname+" "+profile.middlename+" "+profile.lastname
                        cardTopRight.visibility=View.VISIBLE
                        circularGif.visibility=View.VISIBLE
                        CurveRactangle.visibility=View.VISIBLE
                        cardTopLeft.visibility=View.VISIBLE
                        cardBottomLeft.visibility=View.VISIBLE
                        cardBottomRight.visibility=View.VISIBLE
                        layout.visibility=View.VISIBLE
                        navigationView.visibility=View.VISIBLE
                    }
                }
            }
            else if(type=="superAdmin"){
                CoroutineScope(Dispatchers.IO).launch {
                    var profile=Employees.getEmployeeProfile(id)
                    withContext(Dispatchers.Main) {
                        cardTopLeftImg.setBackgroundResource(R.drawable.collaboratormale)
                        cardTopRightImg.setBackgroundResource(R.drawable.faq)
                        cardTopRightText.text="Query!"
                        cardTopLeftText.text="View & Delete User"
                        cardBottomLeftImg.setBackgroundResource(R.drawable.view)
                        cardBottomRightImg.setBackgroundResource(R.drawable.editproperty)
                        cardBottomRightText.text="Update Attendance"
                        cardBottomLeftText.text="View Attendance"
                        circularGif.visibility= View.VISIBLE
                        CurveRactangle.visibility= View.VISIBLE
                        userIdText.text=profile.email
                        if(profile.gender.equals("male")){
                            NavGender.setBackgroundResource(R.drawable.male1)
                            GenderGif.setBackgroundResource(R.drawable.male1)
                        }else if(profile.gender.equals("female")){
                            NavGender.setBackgroundResource(R.drawable.female1)
                            GenderGif.setBackgroundResource(R.drawable.female1)
                        }
                        userNameText.text= profile.firstname+" "+profile.middlename+" "+profile.lastname
                        headerUserIdText.text=profile.email
                        headerUserNameText.text= profile.firstname+" "+profile.middlename+" "+profile.lastname
                        cardTopRight.visibility=View.VISIBLE
                        circularGif.visibility=View.VISIBLE
                        CurveRactangle.visibility=View.VISIBLE
                        cardTopLeft.visibility=View.VISIBLE
                        cardBottomLeft.visibility=View.VISIBLE
                        cardBottomRight.visibility=View.GONE
                        cardBottomRightText.visibility=View.GONE
                        layout.visibility=View.VISIBLE
                        cardBottomLeft
                        navigationView.visibility=View.VISIBLE
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(layout)
                        constraintSet.connect(
                            R.id.CardBottomLeft,
                            ConstraintSet.END,
                            R.id.CardTopRight,
                            ConstraintSet.END,
                            0
                        )
                        constraintSet.connect(
                            R.id.CardBottomLeft,
                            ConstraintSet.START,
                            R.id.CardTopLeft,
                            ConstraintSet.START,
                            0
                        )
                        constraintSet.applyTo(layout)
                    }
                }
            }
            withContext(Dispatchers.Main){
                cardTopRight.visibility=View.GONE
                circularGif.visibility=View.GONE
                CurveRactangle.visibility=View.GONE
                cardTopLeft.visibility=View.GONE
                cardBottomLeft.visibility=View.GONE
                cardBottomRight.visibility=View.GONE
                layout.visibility=View.GONE
                navigationView.visibility=View.GONE
                if (isOnline(this@HomeActivity)){

                }
            }
        }
    }


    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (drawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

}