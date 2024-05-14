package com.example.cosmic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cosmic.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                loginAuthUser(email, password) }else {
                Toast.makeText(this@Login, "Fields cannot be empty", Toast.LENGTH_LONG).show()
            } }
        binding.SignupRedirect.setOnClickListener {
            startActivity(Intent(this@Login, SignupSlidesActivity::class.java))
            finish() }
        binding.forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.forget_dialog, null)
            val email = view.findViewById<EditText>(R.id.email)
            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.btnResetPassword).setOnClickListener {
                compareEmail(email)
                dialog.dismiss() }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss() }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0)) }
            dialog.show() } }

    private fun loginAuthUser(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    MyFunctions.showToast(this@Login, "Login Successful")
                    intent=Intent(this@Login, MainActivity::class.java)
                    intent.putExtra("userEmail",email)
                    startActivity(intent)
                }else{ MyFunctions.showToast(this@Login, "Login Failed") } } }
    private fun compareEmail(email: EditText) {
        if (email.text.toString().isEmpty()) { return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) { return }
        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your email.", Toast.LENGTH_LONG).show()
            } } } }