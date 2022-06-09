package com.example.orderfoodappadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.fragment.*
import com.example.orderfoodappforenterprise.adapter.LoginAdapter
import com.example.orderfoodappforenterprise.adapter.OrderApdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
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
        acceptOderFragment= AcceptOrderFragment()
        declineOrderFragment = DeclineOrderFragment()
        doneOrderFragment = DoneOrderFragment()

        val fragments = arrayListOf(allOrderFragment,acceptOderFragment, declineOrderFragment, doneOrderFragment)
        val adapter = OrderApdapter(fragments, this)
//
        orders_view_pager.adapter = adapter
//
        TabLayoutMediator(orders_tab_layout, orders_view_pager) { tab, position ->
            if (position == 0)
                tab.text = "Pending"
            else if(position == 1)
                tab.text = "Accept"
            else if(position == 2)
                tab.text = "Ignore"
            else if(position == 3)
                tab.text = "Done"
        }.attach()
    }
}