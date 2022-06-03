package com.example.orderfoodappadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.orderfoodappadmin.fragment.LoginFragment
import com.example.orderfoodappadmin.fragment.SignUpFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var loginFragment: Fragment
    private lateinit var signupFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Name", "Thai Binh")
        loginFragment = LoginFragment()
        signupFragment = SignUpFragment()

        val dbRef = FirebaseDatabase.getInstance().getReference("Customer")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children) {
//                    if(data.child("email").value as String == customerEmail) {
//                        providerKey = data.key.toString()
//                        name_textView.text = data.child("name").value as String
//                        address_textView.text = data.child("location").value as String
//                        displayAllFood()
//                        break
//                    }
                    Log.d("data",data.child("email").value as String )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Toast.makeText(this@ProfileActivity, "Cannot load customer's data, please try later!", Toast.LENGTH_LONG).show()
            }

        })


    }
}