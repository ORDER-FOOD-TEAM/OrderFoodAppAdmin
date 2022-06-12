package com.example.orderfoodappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.orderfoodappadmin.activity.ProfileActivity
import com.example.orderfoodappadmin.fragment.LoginFragment
import com.example.orderfoodappadmin.fragment.SignUpFragment
import com.example.orderfoodappforenterprise.adapter.LoginAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginFragment: Fragment
    private lateinit var signupFragment: Fragment
    private lateinit var login_view_pager: ViewPager2
    private lateinit var login_tab_layout: TabLayout

    class KotlinConstantClass {
        companion object {
            var PROVIDER_ID = ""
            var DISHES_ID = ArrayList<String>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login_view_pager = findViewById(R.id.login_view_pager)
        login_tab_layout =  findViewById(R.id.login_tab_layout)
        loginFragment = LoginFragment()
        signupFragment = SignUpFragment()

        val fragments = arrayListOf(loginFragment, signupFragment)
        val adapter = LoginAdapter(fragments, this)

        login_view_pager.adapter = adapter

        TabLayoutMediator(login_tab_layout, login_view_pager) { tab, position ->
            if (position == 0)
                tab.text = "Login"
            else
                tab.text = "Signup"
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        Log.d("User", user.toString())
        if(user != null && user.isEmailVerified){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}