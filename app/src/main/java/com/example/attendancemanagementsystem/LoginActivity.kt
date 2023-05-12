package com.example.attendancemanagementsystem

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employee.api.Students
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private lateinit var btnlogin: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private var type =""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val sharePreference = getSharedPreferences("Credentials", MODE_PRIVATE)
        val id=sharePreference.getString("id","")

        if (id != ""){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        btnlogin = findViewById(R.id.loginButton)
        username = findViewById(R.id.Username)
        password = findViewById(R.id.Password)

        btnlogin.setOnClickListener{
            var Username = username.text.toString()
            var Password = password.text.toString()
            val p = Pattern.compile("\\d{15}")
            val m: Matcher = p.matcher(Username)
            val b: Boolean = m.matches()
            if (b)
            {
                type="students"
            }
            else if (Patterns.EMAIL_ADDRESS.matcher(Username).matches())
            {
                type="employees"
            }

            if(TextUtils.isEmpty(username.text)){
                username.setError("Username cant be Empty")
            }
            else if(TextUtils.isEmpty(password.text))
            {
                password.setError("password cant be Empty")
            }

                CoroutineScope(Dispatchers.IO).launch {
                    val messege=Students.login(Username,Password,type)
                    withContext(Dispatchers.Main){
                        if(!messege.getBoolean("success")){
                            Toast.makeText(this@LoginActivity, messege.getString("messege"), Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val data=messege.getJSONObject("credentials")
                            withContext(Dispatchers.Main) {
                                val sharepreference = getSharedPreferences ("Credentials",MODE_PRIVATE)
                                val editor = sharepreference.edit()
                                val id =data.getString("id")
                                val typeofuser = data.getString("usertype")
                                editor.putString("id",id)
                                editor.putString("type",typeofuser)
                                editor.apply()

                                val intent=Intent(this@LoginActivity,HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                             }
                        }
                    }
                }


            }
        }
    }
