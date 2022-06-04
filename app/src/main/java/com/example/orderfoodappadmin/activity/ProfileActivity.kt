package com.example.orderfoodappadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.orderfoodappadmin.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    lateinit var btn_logout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

//        btn_logout = findViewById(R.id.btn_logout)


//        btn_logout.setOnClickListener(View.OnClickListener {
//            FirebaseAuth.getInstance().signOut();
//        })
    }
}