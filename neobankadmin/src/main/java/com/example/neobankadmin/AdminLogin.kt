package com.example.neobankadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.neobankadmin.databinding.ActivityAdminLoginBinding

class AdminLogin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread.sleep(3000)
        installSplashScreen()

        val aEmail = "neobankowner@gmail.com"
        val aPassword = "admin"

        binding.btn1.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.pass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                if (email == aEmail && password == aPassword) {
                    startActivity(Intent(this@AdminLogin, MainActivity::class.java))
                    finish()
                } else if(email != aEmail) {
                    binding.email.error = "Email is incorrect"
                } else if(password != aPassword) {
                    binding.pass.error = "Password does not match"
                }
            }else {
                Toast.makeText(this@AdminLogin, "Fields cannot be empty", Toast.LENGTH_LONG).show()
            }
        }


    }
}