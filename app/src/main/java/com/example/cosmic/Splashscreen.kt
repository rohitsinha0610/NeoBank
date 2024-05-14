package com.example.cosmic

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.cosmic.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executor

class Splashscreen: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val ref = db.collection("Users").document(userId)
            ref.get().addOnSuccessListener {
                if (it != null) {
                    val name = it.data?.get("Name")?.toString()
                    binding.tv2.text = name
                    checkDeviceHasBiometric()
                }
            }
        } else {
            startActivity(Intent(this, SignupSlidesActivity::class.java))
            finish()
        }

        binding.bioBtn.setOnClickListener {
            checkDeviceHasBiometric()
        }
    }

    private fun biometricListener(){
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, eS: CharSequence) {
                super.onAuthenticationError(errorCode, eS)
                MyFunctions.showToast(applicationContext, "Authentication error: $eS")
            } override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                intActivity()
            } override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                MyFunctions.showToast(this@Splashscreen, "Authentication failed")
            }
        })
    }

    private fun createPromptInfo(){
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Neo Bank")
            .setSubtitle("Please scan your finger to proceed")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
    }

    private fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS ->{
                biometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
            } BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                MyFunctions.showToast(this@Splashscreen, "No biometric features available on this device")
            } BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                MyFunctions.showToast(this@Splashscreen, "Biometric features are currently unavailable")
            } BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                MyFunctions.showToast(this@Splashscreen, "Device not enable Biometric feature")
            } else ->{
                MyFunctions.showToast(this@Splashscreen, "Something went wrong!")
            }
        }
    }

    private fun intActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}