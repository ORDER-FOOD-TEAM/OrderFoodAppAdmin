package com.example.orderfoodappadmin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.orderfoodappadmin.MainActivity
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.fragment.*
import com.example.orderfoodappforenterprise.adapter.LoginAdapter
import com.example.orderfoodappforenterprise.adapter.OrderApdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var allOrderFragment: Fragment
    private lateinit var acceptOderFragment: Fragment
    private lateinit var declineOrderFragment: Fragment
    private lateinit var doneOrderFragment: Fragment
//    private lateinit var signupFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        allOrderFragment = AllOrderFragment()
        acceptOderFragment = AcceptOrderFragment()
        declineOrderFragment = DeclineOrderFragment()
        doneOrderFragment = DoneOrderFragment()


        navView_orders.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.edit_profile -> startActivity(Intent(this, EditProfileActivity::class.java))
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

        menu_button_orders.setOnClickListener {
            orders_drawer_layout.openDrawer(GravityCompat.START)
        }


        val fragments = arrayListOf(
            allOrderFragment,
            acceptOderFragment,
            declineOrderFragment,
            doneOrderFragment
        )
        val adapter = OrderApdapter(fragments, this)
//
        orders_view_pager.adapter = adapter
//
        TabLayoutMediator(orders_tab_layout, orders_view_pager) { tab, position ->
            if (position == 0)
                tab.text = "Pending"
            else if (position == 1)
                tab.text = "Accept"
            else if (position == 2)
                tab.text = "Ignore"
            else if (position == 3)
                tab.text = "Done"
        }.attach()
    }
}
