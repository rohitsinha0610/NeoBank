package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmic.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
//    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var phoneNo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val signupUsername = binding.userName.text.toString()
            val email = binding.signupEmail.text.toString()
            val dob = binding.dob.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()

            if (signupUsername.isNotEmpty() && email.isNotEmpty()
                && dob.isNotEmpty() && password.isNotEmpty() && confirmPass == password){
                //signupUser(signupUsername, email, phoneNo, password)
                intent = Intent(this@SignUp, PhoneNo::class.java)
                intent.putExtra("userName",signupUsername)
                intent.putExtra("userEmail",email)
                intent.putExtra("userDob",dob)
                intent.putExtra("password",password)
                intent.putExtra("confirmPassword",confirmPass)
                startActivity(intent)
            } else if (signupUsername.isNotEmpty() && email.isNotEmpty()
                && dob.isNotEmpty() && password.isNotEmpty() && confirmPass != password){
                MyFunctions.showToast(this@SignUp, "Password does not match")
            }
            else {
                MyFunctions.showToast(this@SignUp, "All fields are mandatory")
            }
        }
    }

//    private fun signupUser(username: String, email: String, phoneNo: String, password: String){
//        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
//            object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (!dataSnapshot.exists()){
//                    val id = databaseReference.push().key
//                    val userData = UserData(id, username, email, phoneNo)
//                    databaseReference.child(id!!).setValue(userData)
//                    authUser(email, password)
//                } else {
//                    MyFunctions.showToast(this@SignUp, "User Already Exists.")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                MyFunctions.showToast(this@SignUp, "Database Error: " +
//                        "${databaseError.message}")
//            }
//        })
//
//    }
//
//    private fun authUser(email: String, password: String){
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) {task ->
//                if (task.isSuccessful){
//                    MyFunctions.showToast(this@SignUp, "Signup Successful")
//                    startActivity(Intent(this@SignUp, AccountCreation::class.java))
//                    finish()
//                }else {
//                    MyFunctions.showToast(this@SignUp, "Signup Unsuccessful")
//                }
//            }
//    }

}