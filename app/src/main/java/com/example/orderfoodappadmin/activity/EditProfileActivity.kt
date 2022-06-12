package com.example.orderfoodappadmin.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.orderfoodappadmin.MainActivity
import com.example.orderfoodappadmin.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    private val PLACE_PICKER_REQUEST = 1
    private var providerEmail = ""
    private var providerID = ""

    private var curLat = 0.0
    private var curLon = 0.0
    private var curAddress = ""
    private var key = ""
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        providerEmail = Firebase.auth.currentUser?.email.toString()

        loadData()

        //Sidebar menu
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView_profile.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.add_food -> startActivity(Intent(this, AddFoodActivity::class.java))
                R.id.sign_out -> {
                    Firebase.auth.signOut()
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    Toast.makeText(applicationContext, "Sign out", Toast.LENGTH_SHORT).show()
                }
                R.id.statistical -> {
                    val intent = Intent(Intent(this, AnalyzeActivity::class.java))
                    startActivity(intent)
                }
            }
            true
        }

        menu_button.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        update_button.setOnClickListener {
            updateData()
        }
    }

    private fun loadData() {
        //load avatar from fire storage
        val customerEmail = Firebase.auth.currentUser?.email.toString()
        val imgName = customerEmail.replace(".", "_")
        val dbRef = FirebaseDatabase.getInstance()
            .getReference("Provider")
            .orderByChild("email")
            .equalTo(providerEmail)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    providerID = data.key.toString()

                    name_inputText.setText(data.child("name").value as String)
                    email_inputText.setText(data.child("email").value as String)
                    phoneNumber_inputText.setText(data.child("phoneNumber").value as String)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateData() {
        val name = name_inputText.text.toString()
        val phoneNumber = phoneNumber_inputText.text.toString()
        val address = address_inputText.text.toString()

        val dbRef = FirebaseDatabase.getInstance().getReference("Provider/$providerID")
        dbRef.child("name").setValue(name)
        dbRef.child("phoneNumber").setValue(phoneNumber)
        dbRef.child("location").setValue(address)
        Toast.makeText(this, "Update successfully!", Toast.LENGTH_LONG).show()
    }
}
