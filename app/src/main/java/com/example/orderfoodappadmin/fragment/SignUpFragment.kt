package com.example.orderfoodappadmin.fragment

import android.content.Intent
import android.os.Bundle
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
import com.example.orderfoodappadmin.activity.ProfileActivity
import com.example.orderfoodappadmin.model.Provider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: CustomDialog

    class KotlinConstantClass {
        companion object {
            var COMPANION_OBJECT_EMAIL = ""
            var COMPANION_OBJECT_PASSWORD = ""
        }
    }


    //Element UI
    lateinit var email_editText: EditText
    lateinit var password_editText: EditText
    lateinit var confirmPassword_editText: EditText
    lateinit var name_edittext: EditText
    lateinit var shopName_editText: EditText
    lateinit var phoneNumber_editText: EditText

    lateinit var signup_button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        dialog = CustomDialog(requireContext())
        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()

        mAuth = Firebase.auth
        val user = mAuth.currentUser

        if (user != null) {
            Log.d("login", user.email.toString())

            val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        email_editText = view.findViewById(R.id.email_editText)
        password_editText = view.findViewById(R.id.password_editText)
        confirmPassword_editText = view.findViewById(R.id.confirmPassword_editText)
        shopName_editText = view.findViewById(R.id.shopName_editText)
        phoneNumber_editText = view.findViewById(R.id.phoneNumber_editText)

        signup_button = view.findViewById(R.id.signup_button)
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = Firebase.auth

        signup_button.setOnClickListener() {
            createProvider()
        }
    }


    private fun createProvider() {
        val email: String = email_editText.text.toString()
        val password: String = password_editText.text.toString()
        val confirmPassword: String = confirmPassword_editText.text.toString()
        val name = shopName_editText.text.toString()
        val phoneNum = phoneNumber_editText.text.toString()
        val address = "Quận 6, Hồ Chí Minh"
        when {
            TextUtils.isEmpty(email) -> {
                email_editText.error = "Email can't be empty"
                email_editText.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                password_editText.error = "Password can't be empty"
                password_editText.requestFocus()
            }
            TextUtils.isEmpty(confirmPassword) -> {
                confirmPassword_editText.error = "You must confirm password"
                confirmPassword_editText.requestFocus()
            }
            TextUtils.isEmpty(name) -> {
                email_editText.error = "Name can't be empty"
                email_editText.requestFocus()
            }
            TextUtils.isEmpty(phoneNum) -> {
                password_editText.error = "Phone number can't be empty"
                password_editText.requestFocus()
            }
            TextUtils.isEmpty(address) -> {
                confirmPassword_editText.error = "Address can't be empty"
                confirmPassword_editText.requestFocus()
            }
            password != confirmPassword -> {
                confirmPassword_editText.error = "Your Confirm is not correct. Please confirm again"
                confirmPassword_editText.requestFocus()
            }
            else -> {
                dialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            //send email verify
                            Firebase.auth.currentUser?.sendEmailVerification()

                            // Sign in success, update UI with the signed-in user's information
                            createProviderData(email, name, phoneNum, address)

                            KotlinConstantClass.COMPANION_OBJECT_EMAIL = email
                            KotlinConstantClass.COMPANION_OBJECT_PASSWORD = password

                            Toast.makeText(
                                requireActivity(),
                                "User sign up successfully, please check mail to verify account!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent =
                                Intent(requireActivity(), ProfileActivity::class.java)
                            startActivity(intent)
                            if (dialog.isShowing()) {
                                dialog.dismiss()
                            }
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss()
                            }
                            Toast.makeText(
                                requireActivity(), "Sign Up Error: " + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun createProviderData(email: String, name: String, phoneNum: String, address: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Provider")
        val newProvider = Provider(
            email,
            address,
            name,
            phoneNum,
            "0"
        )
        val key = dbRef.push().key.toString()
        dbRef.child(key).setValue(newProvider)
    }
}