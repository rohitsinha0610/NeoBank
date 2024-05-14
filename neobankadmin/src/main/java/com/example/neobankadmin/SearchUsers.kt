package com.example.neobankadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.neobankadmin.databinding.ActivitySearchUsersBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchUsers : AppCompatActivity() {

    private lateinit var binding: ActivitySearchUsersBinding

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            val email = binding.search.text.toString()

            if (email != null) {
                val usersCollection = db.collection("Users")
                val query: Query = usersCollection.whereEqualTo("Email", email)
                query.get().addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        // UPI ID not found
                        MyFunctions.showToast(this@SearchUsers, "User not found")
//                        startActivity(Intent(this@SearchUsers, MainActivity::class.java))
//                        finish()
                        // Handle the case when the UPI ID is not associated with any user.
                    } else {
                        binding.ll2.visibility = View.VISIBLE

                        val document = querySnapshot.documents[0]
                        val name = document.getString("Name").toString()
                        val accNo = document.getString("AccountNumber").toString()
                        val mobNo = document.getString("MobileNo").toString()
                        val state = document.getString("State").toString()
                        val accId = document.getString("AccountID").toString()
                        val cardId = document.getString("CardID").toString()

                        binding.name.text = name.toString()
                        binding.accNo.text = accNo.toString()
                        binding.mobNo.text = mobNo.toString()
                        binding.state.text = state.toString()
                        binding.accId.text = accId.toString()
                        binding.cardId.text = cardId.toString()

                    }
                }
                    .addOnFailureListener { exception ->
                        MyFunctions.showToast(this@SearchUsers, "Database Error")
                    }
            } else {
                MyFunctions.showToast(this@SearchUsers, "Please enter email.")
            }
        }
    }
}