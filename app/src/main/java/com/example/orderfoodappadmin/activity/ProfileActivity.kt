package com.example.orderfoodappadmin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.orderfoodappadmin.MainActivity
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.adapter.DishAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private var providerKey = ""
    private lateinit var dishAdapter: DishAdapter
    lateinit var toggle: ActionBarDrawerToggle
    private var providerEmail = Firebase.auth.currentUser?.email.toString()
    private var providerId = ""
    private var dishesId = mutableListOf<String>()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        navView_profile.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.add_food -> startActivity(Intent(this, AddFoodActivity::class.java))
//                R.id.edit_profile -> startActivity(Intent(this, EditProfileActivity::class.java))
                R.id.sign_out -> {
                    Firebase.auth.signOut()
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    Toast.makeText(applicationContext, "Sign out", Toast.LENGTH_SHORT).show()
                }
//                R.id.statistical -> {
//                    val intent = Intent(Intent(this, AnalyzeActivity::class.java))
//                    startActivity(intent)
//                }
                R.id.orders -> {
                    Toast.makeText(applicationContext, "Come here order", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Intent(this, OrdersActivity::class.java))
                    startActivity(intent)
                }
            }
            true
        }

        menu_button.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }
}
