package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cosmic.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityOtpBinding

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNo: String
    private lateinit var signupUsername: String
    private lateinit var email: String
    private lateinit var dob: String
    private lateinit var password: String
    private lateinit var confirmPass: String
    //private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        signupUsername = intent?.extras?.getString("userName").toString()
        email = intent?.extras?.getString("userEmail").toString()
        dob = intent?.extras?.getString("userDob").toString()
        password = intent?.extras?.getString("password").toString()
        confirmPass = intent?.extras?.getString("confirmPassword").toString()
        phoneNo = intent?.extras?.getString("phoneNo").toString()

        binding.pnBlankText.text = this.phoneNo

        sendOtp(phoneNo,false)

        binding.nextBtn.setOnClickListener {
            val enteredOtp: String = binding.mobileNoOtp.text.toString()
            val credentials: PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, enteredOtp)
            signIn(credentials)
        }

        binding.resendOtp.setOnClickListener {
            sendOtp(phoneNo,true)
        }
    }

    private fun sendOtp(phoneNumber: String, isResend: Boolean){
        startResendTimer()
        setInProgress(true)  
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signIn(credential)
                    //signupUser(signupUsername, email, phoneNo, dob, password)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    MyFunctions.showToast(this@OtpActivity, "Verification Failed: $e")
                    setInProgress(false)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    MyFunctions.showToast(this@OtpActivity, "OTP sent successfully")
                    setInProgress(false)
                }
            })
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendToken).build())
        }else {
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
    }

    private fun setInProgress(inProgress: Boolean){
        if(inProgress){
            binding.progressBarOtp.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
        }else{
            binding.progressBarOtp.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential){
        setInProgress(true)
        auth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(this) { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    signupUser(signupUsername, email, phoneNo, dob, password)
                } else {
                    MyFunctions.showToast(this@OtpActivity, "OTP verification failed!")
                }
            }
    }

    private fun startResendTimer(){
        binding.resendOtp.isEnabled = false
        val timer = Timer()
        var timeoutSeconds = 60L
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    timeoutSeconds--
                    runOnUiThread {
                        binding.resendOtp.text = "Resend OTP in $timeoutSeconds Seconds"
                    }
                    if (timeoutSeconds <= 0) {
                        timeoutSeconds = 60L
                        timer.cancel()
                        runOnUiThread {
                            binding.resendOtp.isEnabled = true
                        }
                    }
                }
            },
            0,
            1000
        )
    }

    private fun signupUser(username: String, email: String, phoneNo: String, dob: String, password: String){
        //authUser(email, password)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){

                    MyFunctions.showToast(this@OtpActivity, "Signup Successful")
                    intent = Intent(this@OtpActivity, KYC1::class.java)
                    intent.putExtra("userName",username)
                    intent.putExtra("userEmail",email)
                    intent.putExtra("userDob",dob)
                    intent.putExtra("phoneNo",phoneNo)
                    startActivity(intent)

                }else {
                    MyFunctions.showToast(this@OtpActivity, "Signup Unsuccessful")
                }
            }



//        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
//            object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    if (!dataSnapshot.exists()){
//                        userId = databaseReference.push().key.toString()
//                        val userData = UserData(userId, username, email, phoneNo, dob)
//                        databaseReference.child(userId!!).setValue(userData)
//                        authUser(email, password)
//                    } else {
//                        MyFunctions.showToast(this@OtpActivity, "User Already Exists.")
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    MyFunctions.showToast(this@OtpActivity, "Database Error: " +
//                            "${databaseError.message}")
//                }
//            })

    }

//    private fun authUser(email: String, password: String){
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) {task ->
//                if (task.isSuccessful){
//                    MyFunctions.showToast(this@OtpActivity, "Signup Successful")
//                    intent = Intent(this@OtpActivity, KYC1::class.java)
//                    intent.putExtra("UserId",userId)
//                    intent.putExtra("userEmail",email)
//                    startActivity(intent)
//                }else {
//                    MyFunctions.showToast(this@OtpActivity, "Signup Unsuccessful")
//                }
//            }
//    }

}