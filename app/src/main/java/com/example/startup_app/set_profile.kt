 package com.example.startup_app

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContentResolverCompat.query
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

 data class Image(val uri: Uri ,
    val  name:String ,
    val size:Int
)

 private val imageList = mutableListOf<Image>()


 class set_profile : AppCompatActivity() {
    val SELECT_PICTURE =100
    private val user  = Firebase.auth.currentUser
    private lateinit var profile_img : ImageView
    private lateinit var change_img : Button
    private lateinit var save_changes : Button
    private lateinit var imageUri:Uri
    private lateinit var getName:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile)

        getName = findViewById(R.id.edit_name)

        profile_img = findViewById(R.id.select_img)
        change_img = findViewById(R.id.select_photo)
        save_changes = findViewById(R.id.save)

        change_img.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@set_profile , "open gallery" , Toast.LENGTH_SHORT).show()
                imageCloser()
            }
        })

        save_changes.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = getName.text.toString()
                    photoUri = imageUri
                }

                user!!.updateProfile(profileUpdates).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@set_profile ,"Save changes" , Toast.LENGTH_SHORT).show()

                    }
                }
            }

        })
    }

    fun imageCloser(){
        intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"Select Pictire") ,SELECT_PICTURE)
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if(resultCode == RESULT_OK){
             if (requestCode == SELECT_PICTURE){
                 imageUri= data?.data!!
                 if (null!= imageUri){
                     profile_img.setImageURI(imageUri)
                 }
             }
         }
     }

    companion object{
        private const val TAG = "set profile"
    }
}
