package com.example.cosmic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.cosmic.databinding.ActivityQrcodeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream

class QRcode : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityQrcodeBinding
    private lateinit var upiId: String
    private lateinit var name: String
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            startActivity(Intent(this@QRcode, MainActivity::class.java))
        }

        binding.OpenScanBtn.setOnClickListener {
            startActivity(Intent(this@QRcode, Scan::class.java))
        }

        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid
        val ref = db.collection("Users").document(userId.toString())
        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                name = documentSnapshot.get("Name").toString()
                upiId = documentSnapshot.get("UPI_ID").toString()
                val data = "upi://pay?$upiId"
                bitmap = generateQRCode(data, 260)
                runOnUiThread {
                    binding.qRtv.setImageBitmap(bitmap)
                    binding.nametv.text = name
                    binding.upi.text = upiId
                }
            }
        }

        binding.shareBtn.setOnClickListener {
            val combinedBitmap = createCombinedBitmap(name, upiId, bitmap)
            val jpgFilePath = saveBitmapToJpg(combinedBitmap)
            shareImage(jpgFilePath)
        }

    }

    private fun generateQRCode(data: String, size: Int): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun createCombinedBitmap(userName: String, upiId: String, qrCodeBitmap: Bitmap): Bitmap {
        val width = 360
        val height = 400

        val combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(combinedBitmap)
        canvas.drawColor(Color.WHITE)

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 24f

        val yPos = 20f
        canvas.drawBitmap(qrCodeBitmap, 20f, yPos + 100f, null)
        canvas.drawText(userName, 20f, yPos, textPaint)
        canvas.drawText("UPI ID: $upiId", 20f, yPos + 30f, textPaint)

        return combinedBitmap
    }

    private fun saveBitmapToJpg(bitmap: Bitmap): String {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "combined_image.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file.absolutePath
    }

    private fun shareImage(imagePath: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        val imageUri = FileProvider.getUriForFile(this, "$packageName.provider", File(imagePath))
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
}