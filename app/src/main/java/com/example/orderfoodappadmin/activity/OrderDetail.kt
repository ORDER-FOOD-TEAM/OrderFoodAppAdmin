package com.example.orderfoodappadmin.activity

import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orderfoodappadmin.CustomDialog
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.adapter.BillAdapter
import com.example.orderfoodappadmin.model.BillItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_order_detail.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class OrderDetail : AppCompatActivity() {
    private lateinit var billAdapter: BillAdapter
    private val df = DecimalFormat("##.##")
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    private val sdf2 = SimpleDateFormat("EEE, d MMM yyyy")
    private var id = ""
    private var customerEmail = ""
    private var status = ""
    private lateinit var dialog: CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        dialog = CustomDialog(applicationContext)

        id = intent.getStringExtra("id").toString()
        status = intent.getStringExtra("status").toString()
        customerEmail = intent.getStringExtra("customerEmail").toString()

        Log.d("BBB", "customerEmail" + customerEmail)
        Log.d("BBB", "status" + status)
        Log.d("BBB", "id" + id)

        billAdapter = BillAdapter(mutableListOf())
        billDetail_recyclerView.adapter = billAdapter

        val layoutManager = LinearLayoutManager(this)
        billDetail_recyclerView.layoutManager = layoutManager

        displayProducts()
        displayBill()
        displayCustomerInfor()



        if ( status == "Accept" ) {
            decline_btn.visibility = View.INVISIBLE
            accept_btn.visibility = View.INVISIBLE
            done_btn.visibility = View.VISIBLE
            delete_order.visibility = View.INVISIBLE
        } else if (status == "Pending") {
            delete_order.visibility = View.INVISIBLE
        } else {
            decline_btn.visibility = View.INVISIBLE
            accept_btn.visibility = View.INVISIBLE
        }
        back_layout.setOnClickListener() {
            finish()
        }

        decline_btn.setOnClickListener() {
            updateDeclineOrderStatus()
            finish()
        }
        accept_btn.setOnClickListener() {
            updateAcceptOrderStatus()
            finish()
        }
        delete_order.setOnClickListener() {
            deleteDeclineOrderStatus()
            finish()
        }
        done_btn.setOnClickListener(){
            doneDeclineOrderStatus()
            finish()
        }
    }

    private fun updateDeclineOrderStatus() {

        val dbRef = FirebaseDatabase.getInstance().getReference("Bill/$id")
        dbRef.child("status").setValue("Decline")

        Toast.makeText(
            applicationContext,
            "Update status successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteDeclineOrderStatus() {

        val dbRef = FirebaseDatabase.getInstance().getReference("Bill/$id")
        dbRef.child("status").setValue("Deleted")

        Toast.makeText(
            applicationContext,
            "Delete order successfully",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun doneDeclineOrderStatus() {

        val dbRef = FirebaseDatabase.getInstance().getReference("Bill/$id")
        dbRef.child("status").setValue("Done")

        Toast.makeText(
            applicationContext,
            "Order has been shipped",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateAcceptOrderStatus() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Bill/$id")
        dbRef.child("status").setValue("Accept")
        Toast.makeText(
            applicationContext,
            "Update status successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun displayProducts() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Bill/$id/products")
        dbRef.get().addOnSuccessListener {
            for (data in it.children) {
                var unitPrice = 0.0
                val a: Any = data.child("unitPrice").value as Any
                val type = a::class.simpleName
                if (type == "Long" || type == "Double")
                    unitPrice = a.toString().toDouble()

                val bill = BillItem(
                    data.child("amount").value as Long,
                    data.child("name").value as String,
                    unitPrice
                )
                billAdapter.addBill(bill)
            }
        }
    }


    private fun displayBill() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Bill/$id")
        dbRef.get().addOnSuccessListener {
            var subTotal = 0.0
            val a: Any = it.child("subTotal").value as Any
            val typeA = a::class.simpleName
            if (typeA == "Long" || typeA == "Double")
                subTotal = a.toString().toDouble()

            var total = 0.0
            val b: Any = it.child("total").value as Any
            val typeB = b::class.simpleName
            if (typeB == "Long" || typeB == "Double")
                total = b.toString().toDouble()

            val deliveryFee = total - subTotal

            val date = sdf1.parse(it.child("time").value as String)
            val formattedDate = sdf2.format(date)

            sub_text.text = "$$subTotal"
            total_text.text = "$$total"
            deliveryFee_text.text = "$${df.format(deliveryFee)}"
            date_text.text = formattedDate

            status_text.text = it.child("status").value.toString()

            when (it.child("status").value as String) {
                "Pending" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    status_text.setTextColor(getColor(R.color.yellow))
                }
                "Decline" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    status_text.setTextColor(getColor(R.color.red))
                }
                else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    status_text.setTextColor(getColor(R.color.orange_main))
                }

            }
        }
    }

    private fun displayCustomerInfor() {

        val dbRef = FirebaseDatabase.getInstance().getReference("Customer")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("email").value as String == customerEmail) {
                        orderid_text.text = id
                        name_text.text = data.child("fullName").value.toString()
                        phone_text.text = data.child("phoneNumber").value.toString()
                        address_text.text = data.child("address").value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}