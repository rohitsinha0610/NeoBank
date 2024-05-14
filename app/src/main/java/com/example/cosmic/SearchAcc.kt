package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmic.databinding.ActivitySearchAccBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchAcc : AppCompatActivity() {

    private lateinit var binding: ActivitySearchAccBinding
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    //private lateinit var name: String
    //private lateinit var oid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchAccBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        //val uid = auth.currentUser?.uid.toString()
        binding.cancel.setOnClickListener {
            startActivity(Intent(this@SearchAcc, MainActivity::class.java))
            finish()
        }



        val searchr = binding.search.text.toString()





        // Assuming you have the user IDs and transfer amount




    }
}