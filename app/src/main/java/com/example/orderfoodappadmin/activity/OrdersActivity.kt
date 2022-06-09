package com.example.orderfoodappadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.fragment.AllOrderFragment
import com.example.orderfoodappadmin.fragment.LoginFragment
import com.example.orderfoodappadmin.fragment.SignUpFragment
import com.example.orderfoodappforenterprise.adapter.LoginAdapter
import com.example.orderfoodappforenterprise.adapter.OrderApdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var allOrderFragment: Fragment
    private lateinit var allOrderFragment1: Fragment
    private lateinit var allOrderFragment2: Fragment
    private lateinit var allOrderFragment3: Fragment
//    private lateinit var signupFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        allOrderFragment = AllOrderFragment()
        allOrderFragment1 = AllOrderFragment()
        allOrderFragment2 = AllOrderFragment()
        allOrderFragment3 = AllOrderFragment()

        val fragments = arrayListOf(allOrderFragment,allOrderFragment1, allOrderFragment2, allOrderFragment3)
        val adapter = OrderApdapter(fragments, this)
//
        orders_view_pager.adapter = adapter
//
        TabLayoutMediator(orders_tab_layout, orders_view_pager) { tab, position ->
            if (position == 0)
                tab.text = "All"
            else if(position == 1)
                tab.text = "Accept"
            else if(position == 2)
                tab.text = "Ignore"
            else if(position == 3)
                tab.text = "Done"
        }.attach()
    }
}