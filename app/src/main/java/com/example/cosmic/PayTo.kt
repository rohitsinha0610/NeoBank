package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.cosmic.databinding.ActivityPayToBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Random

class PayTo : AppCompatActivity() {

    private lateinit var binding: ActivityPayToBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var userId: String
    private lateinit var otherUserId: String
    private lateinit var payEditText: EditText
    private lateinit var senderName: String
    private lateinit var receiverName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayToBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        userId = auth.currentUser?.uid.toString()

        otherUserId = intent?.extras?.getString("otherUserId").toString()

        val ref = db.collection("Users").document(otherUserId.toString())
        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val name = documentSnapshot.get("Name").toString()
//                cardNumber = documentSnapshot.get("CardNumber").toString()
//                cardExpiryDate = documentSnapshot.get("CardExpiryDate").toString()
//                cardCvv = documentSnapshot.get("CardCvv").toString()
//                upiId = documentSnapshot.get("UPI_ID").toString()
//                bal = documentSnapshot.get("AccountBalance").toString()

                runOnUiThread {
                    binding.nameTv.text = name
//                    binding.cardExpDate.text = cardExpiryDate
//                    binding.cardNumber.text = cardNumber
//                    binding.holderName.text = name
//                    binding.upi.text = upiId
//                    binding.balance.text = bal
                }
            }
        }

        payEditText = findViewById(R.id.pay)

        binding.cancel.setOnClickListener {
            startActivity(Intent(this@PayTo, MainActivity::class.java))
            finish()
        }

        val transferButton = findViewById<Button>(R.id.transferButton) 
        transferButton.setOnClickListener {
            handleTransaction()
        }
    }

    private fun handleTransaction() {
        val transferAmountStr = payEditText.text.toString()
        if (transferAmountStr.isNotEmpty()) {
            val transferAmount = transferAmountStr.toInt()

            if (transferAmount <= 60000){
                val senderUserId = userId
                val receiverUserId = otherUserId

                val senderUserRef = db.collection("Users").document(senderUserId)
                val receiverUserRef = db.collection("Users").document(receiverUserId)

                db.runTransaction { transaction ->
                    val senderDoc = transaction.get(senderUserRef)
                    val receiverDoc = transaction.get(receiverUserRef)

                    senderName = senderDoc.getString("Name").toString()
                    receiverName = receiverDoc.getString("Name").toString()

                    //binding.nameTv.text = "Paying $receiverName"
                    binding.nameTv.text = receiverName

                    val senderBalance = senderDoc.getDouble("AccountBalance") ?: 0.0
                    if (senderBalance < transferAmount) {
                        MyFunctions.showToast(this, "Insufficient balance")
                        throw Exception("Insufficient balance")
                    }

                    // Update the sender's balance
                    val newSenderBalance = senderBalance - transferAmount
                    transaction.update(senderUserRef, "AccountBalance", newSenderBalance)

                    // Update the receiver's balance
                    val receiverBalance = receiverDoc.getDouble("AccountBalance") ?: 0.0
                    val newReceiverBalance = receiverBalance + transferAmount
                    transaction.update(receiverUserRef, "AccountBalance", newReceiverBalance)

                    // Save transaction details to Firestore
                }.addOnSuccessListener {
                    val transactionId = generate12DigitTransactionId()
                    val tMethod = "QR Code"
                    val note = findViewById<EditText>(R.id.note)

                    val transactionData = hashMapOf(
                        "TransactionId" to transactionId,
                        "TransactionMethod" to tMethod,
                        "SenderUserId" to senderUserId,
                        "ReceiverUserId" to receiverUserId,
                        "SenderName" to senderName,
                        "ReceiverName" to receiverName,
                        "Amount" to transferAmount,
                        "Note" to note.text.toString(),
                        "Timestamp" to Timestamp.now()
                    )

                    //val paid:Boolean = true

                    db.collection("Transactions").document(transactionId.toString())
                        .set(transactionData)
                        .addOnSuccessListener {
                            //MyFunctions.showToast(this, "Transaction Successful")
                            intent = Intent(this@PayTo, Tranctiondetail::class.java)
                            intent.putExtra("senderName",senderName)
                            intent.putExtra("receiverName",receiverName)
                            intent.putExtra("tid",transactionId)
                            intent.putExtra("tAmount",transferAmount)
                            intent.putExtra("note",note.text.toString())
                            intent.putExtra("tAmount",transferAmount)
                            intent.putExtra("paid",true)
                            startActivity(intent)
                        }.addOnFailureListener{
                            //MyFunctions.showToast(this, "Transaction Unsuccessful inner")
                            //MyFunctions.showToast(this, "Transaction Unsuccessful")
                            intent = Intent(this@PayTo, Tranctiondetail::class.java)
                            intent.putExtra("senderName",senderName)
                            intent.putExtra("receiverName",receiverName)
                            intent.putExtra("tid",transactionId)
                            intent.putExtra("tAmount",transferAmount)
                            intent.putExtra("note",note.text.toString())
                            intent.putExtra("tAmount",transferAmount)
                            intent.putExtra("paid",false)
                            startActivity(intent)
                        }

                }.addOnFailureListener {
                    MyFunctions.showToast(this, "Transaction Unsuccessful")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } else {
                MyFunctions.showToast(this, "Please enter a valid amount (<= 60000)")
            }

        } else {
            MyFunctions.showToast(this, "Please enter a valid amount")
        }
    }

    private fun generate12DigitTransactionId(): Long {
        val minId = 1_000_000_000_000 // 12-digit minimum
        val maxId = 9_999_999_999_999 // 12-digit maximum
        return Random().nextInt((maxId - minId + 1).toInt()) + minId
    }
}
