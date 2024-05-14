package com.example.cosmic

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cosmic.databinding.ActivityPayToBinding
import com.example.cosmic.databinding.ActivityTranctiondetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Tranctiondetail : AppCompatActivity() {

    private lateinit var binding: ActivityTranctiondetailBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var senderName: String
    private lateinit var receiverName: String
    private lateinit var tid: String
    private lateinit var tAmount: String
    private lateinit var note: String
    //val paid: Boolean = false

//    intent.putExtra("senderName",senderName)
//    intent.putExtra("receiverName",receiverName)
//    intent.putExtra("tid",transactionId)
//    intent.putExtra("tAmount",transferAmount)
//    intent.putExtra("note",note.text.toString())
//    intent.putExtra("tAmount",transferAmount)
//    intent.putExtra("paid",true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranctiondetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        senderName = intent?.extras?.getString("senderName").toString()
        receiverName = intent?.extras?.getString("receiverName").toString()
        tid = intent?.extras?.getLong("tid").toString()
        tAmount = intent?.extras?.getInt("tAmount").toString()
        note = intent?.extras?.getString("note").toString()
        val paid = intent?.extras?.getBoolean("paid")

        if (paid == true) {
            binding.imgPaid.visibility = View.VISIBLE
            binding.tid.text = "Transaction ID: $tid"
            binding.tAmount.text = "Transaction Amount: $tAmount"
            binding.senderName.text = "Sender: $senderName"
            binding.receiverName.text = "Receiver: $receiverName"
            binding.note.text = "Note: $note"
        }else {
            binding.paidText.text = "Transaction Unsuccessfull"
            binding.tid.text = "Transaction ID: $tid"
            binding.tAmount.text = "Transaction Amount: $tAmount"
            binding.senderName.text = "Sender: $senderName"
            binding.receiverName.text = "Receiver: $receiverName"
            binding.note.text = "Receiver: $note"
        }

        binding.doneBtn.setOnClickListener {
            startActivity(Intent(this@Tranctiondetail, MainActivity::class.java))
            finish()
        }
    }
}