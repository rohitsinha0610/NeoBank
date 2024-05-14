package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmic.databinding.ActivityPhoneNoBinding

class PhoneNo : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNoBinding
    private lateinit var signupUsername: String
    private lateinit var email: String
    private lateinit var dob: String
    private lateinit var password: String
    private lateinit var confirmPass: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhoneNoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signupUsername = intent?.extras?.getString("userName").toString()
        email = intent?.extras?.getString("userEmail").toString()
        dob = intent?.extras?.getString("userDob").toString()
        password = intent?.extras?.getString("password").toString()
        confirmPass = intent?.extras?.getString("confirmPassword").toString()

        binding.countryCode.registerCarrierNumberEditText(binding.mobileNo)

        binding.sendOtpBtn.setOnClickListener { v ->
            if(!binding.countryCode.isValidFullNumber){
                binding.mobileNo.error = "Phone No is Invalid!"
                return@setOnClickListener
            }
            intent = Intent(this@PhoneNo, OtpActivity::class.java)
            intent.putExtra("userName",signupUsername)
            intent.putExtra("userEmail",email)
            intent.putExtra("userDob",dob)
            intent.putExtra("password",password)
            intent.putExtra("confirmPassword",confirmPass)
            intent.putExtra("phoneNo",binding.countryCode.fullNumberWithPlus)
            startActivity(intent)
        }
    }
}