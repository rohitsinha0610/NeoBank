package com.example.cosmic

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cosmic.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding
    private lateinit var name: String
    private lateinit var dob: String
    private lateinit var email: String
    private lateinit var mNumber: String
    private lateinit var state: String
    private lateinit var district: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.back.setOnClickListener {
            startActivity(Intent(this@Profile, MainActivity::class.java))
            finish()
        }

        val userId = auth.currentUser?.uid
        val ref = db.collection("Users").document(userId.toString())

        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                name = documentSnapshot.get("Name").toString()
                dob = documentSnapshot.get("DateOfBirth").toString()
                email = documentSnapshot.get("Email").toString()
                mNumber = documentSnapshot.get("MobileNo").toString()
                state = documentSnapshot.get("State").toString()
                district = documentSnapshot.get("District").toString()

                runOnUiThread {
                    binding.dob.text = dob
                    binding.phone.text = mNumber
                    binding.email.text = email
                    binding.name.text = name
                    binding.address.text = "$state, $district"

                }
            }
        }

    }
}