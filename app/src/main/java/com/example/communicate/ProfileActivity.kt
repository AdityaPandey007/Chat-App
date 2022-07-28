package com.example.communicate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity() {

    // declaring variables which gonna used in this activity
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    var selectedImage : Uri? = null
    //  var dialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()

        /*
        //progress bar dialog
        dialog!!.setMessage("Update Profile...")
        dialog!!.setCancelable(false)
         */

        //initializing firebase's stuffs
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        // check the current user whether he logged in or not
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        // code logic for circular imageView
        imgProfile.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/"
            startActivityForResult(intent,45)
        }

        //code logic on button click
        btnProfile.setOnClickListener {

            //warning to fill name properly
            val naame = edtName.text.toString()
            if (naame.isEmpty()){
                edtName.error = "Please enter name first!!"
            }

            //code logic for image adding
            if (selectedImage != null){
                val reference = storage.reference.child("Profile")
                    .child(auth.uid!!)
                reference.putFile(selectedImage!!).addOnCompleteListener{ task ->

                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnCompleteListener { uri ->
                            val imageUrl = uri.toString()
                            val uid = auth.uid
                            val name: String = edtName.text.toString()
                            val phone = auth.currentUser!!.phoneNumber
                            val user = User(uid, name, phone,imageUrl)

                            database.reference.child("users")

                                .child(uid!!)
                                .setValue(user)
                                .addOnCompleteListener {
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }
                    else {
                        val uid = auth.uid
                        val phone = auth.currentUser!!.phoneNumber
                        val name: String = edtName.text.toString()
                        val user = User(uid , name , phone , "No Image")

                        database.reference
                            .child("users")
                            .child(uid!!)
                            .setValue(user)
                            .addOnCompleteListener {
                                val intent = Intent(this , MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if(data.data != null){
                val uri = data.data // filePath
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference.child("Profile").child(time.toString() + "")

                reference.putFile(uri!!).addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        reference.downloadUrl.addOnCompleteListener { uri ->
                            val filePath = uri.toString()
                            val obj = HashMap<String ,Any>()
                            obj["image"] = filePath
                            database.reference
                                .child("users")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(obj).addOnSuccessListener {  }
                        }
                    }
                }
                imgProfile.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }
}