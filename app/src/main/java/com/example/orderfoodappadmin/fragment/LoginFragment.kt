package com.example.orderfoodappadmin.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.orderfoodappadmin.CustomDialog
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.activity.AddFoodActivity
import com.example.orderfoodappadmin.activity.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: CustomDialog

    //Element UI
    lateinit var email_editText: EditText
    lateinit var password_editText: EditText
    lateinit var login_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        email_editText = view.findViewById(R.id.email_editText)
        password_editText = view.findViewById(R.id.password_editText)
        login_button = view.findViewById(R.id.login_button)

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d("Come Start", "Come Start")

        mAuth = Firebase.auth
        val user = mAuth.currentUser

        if (user != null && user.isEmailVerified) {
            val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Come Start", "onResume")
        mAuth = Firebase.auth
        val user = mAuth.currentUser

        if (user != null) {
            Log.d("login", user.email.toString())

            val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }

        //init loading dialog
        dialog = CustomDialog(requireContext())
        login_button.setOnClickListener {
            dialog.show()
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = email_editText.text.toString()
        val password: String = password_editText.text.toString()
        Log.d("email", email)
        Log.d("password", password)

        when {
            TextUtils.isEmpty(email) -> {
                email_editText.error = "Email can't be empty"
                email_editText.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                password_editText.error = "Password can't be empty"
                password_editText.requestFocus()
            }
            else -> {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            //set delay for smooth animation
                            val handler = Handler()
                            handler.postDelayed({
                                if (dialog.isShowing()) {
                                    dialog.dismiss()
                                }
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(
                                    requireActivity(),
                                    "User logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(requireActivity(), ProfileActivity::class.java)
                                startActivity(intent)
                            }, 1000)
//                            }
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss()
                            }
                            Toast.makeText(
                                requireActivity(),
                                "Login Error: " + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}