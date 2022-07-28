package com.example.communicate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_otp.*


class OtpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        auth = FirebaseAuth.getInstance()

        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        verifyOtp.setOnClickListener {
            var otp = edtOtp.text.toString().trim()

            if (otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId.toString() ,otp)
                signInWithPhoneAuthCredential(credential)
            }
            else {
                Toast.makeText(this, "Enter Otp", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){

                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                }
                else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(applicationContext, "Invalid Otp", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}