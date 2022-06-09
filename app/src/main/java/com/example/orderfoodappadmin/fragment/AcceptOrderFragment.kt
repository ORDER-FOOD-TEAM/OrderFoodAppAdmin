package com.example.orderfoodappadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orderfoodapp.adapters.AllOrderAdapter
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_all_order.*
import java.text.SimpleDateFormat


class AcceptOrderFragment : Fragment() {
    private lateinit var acceptOrderAdapter: AllOrderAdapter
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    private val sdf2 = SimpleDateFormat("EEE, d MMM yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        acceptOrderAdapter = AllOrderAdapter(mutableListOf())
        rvOrderList.adapter = acceptOrderAdapter

        val layoutManager = LinearLayoutManager(context)
        rvOrderList.layoutManager = layoutManager

        val dbRef = FirebaseDatabase.getInstance().getReference("Bill")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                acceptOrderAdapter.deleteAll()
                for (data in snapshot.children) {
                    if ( data.child("status").value?.equals("In cart") != true
                        && data.child("status").value?.equals("Accept") == true) {
                        var total = 0.0
                        val a: Any = data.child("total").value as Any
                        val type = a::class.simpleName
                        if(type == "Long" || type == "Double")
                            total = a.toString().toDouble()
                        val status = data.child("status").value.toString();
                        val dbRef2 = FirebaseDatabase.getInstance().getReference("Bill/${data.key}/products")
                        dbRef2.get().addOnSuccessListener {
                            val count = it.childrenCount.toInt()

                            val date = sdf1.parse(data.child("time").value as String)
                            val formattedDate = sdf2.format(date)

                            val order = Order(
                                data.key.toString(),
                                total,
                                count,
                                formattedDate,
                                status
                            )
                            acceptOrderAdapter.addOrder(order)
                        }

                    }
                }
                //set animation
                val layoutAnim = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_left_to_right)
                rvOrderList.layoutAnimation = layoutAnim
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}