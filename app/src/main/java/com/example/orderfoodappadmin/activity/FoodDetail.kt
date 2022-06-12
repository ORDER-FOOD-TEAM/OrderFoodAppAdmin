package com.example.orderfoodappadmin.activity

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.model.Dish
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_food_detail.*
import java.io.File
import java.lang.Exception

class FoodDetail : AppCompatActivity() {
    private lateinit var providerEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        providerEmail = Firebase.auth.currentUser?.email.toString()
        val curDish = intent.getParcelableExtra<Dish>("curDish")


        if (curDish != null) {
            //load food image
            val storageRef =
                FirebaseStorage.getInstance().getReference("dish_image/${curDish.id}.jpg")
            try {
                val localFile = File.createTempFile("tempfile", ".jpg")
                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    food_image.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            loadDish(curDish)
        }
        edit_button.setOnClickListener {
            enableEdit()
        }

        btnUpdate.setOnClickListener {
            updateDish(curDish!!)
        }

        back_button.setOnClickListener {
            finish()
        }


    }

    private fun loadDish(curDish: Dish) {
        foodName_text.text = curDish.name
        rates_text.text = curDish.rated

        priceS_textView.setText(curDish.priceS.toString())
        priceM_textView.setText(curDish.priceM.toString())
        priceL_textView.setText(curDish.priceL.toString())

        num_sold_textView.setText(curDish.amountSsold.toString())
        numMsold_textView.setText(curDish.amountMsold.toString())
        numLsold_textView.setText(curDish.amountLsold.toString())

        saleOff_textView.setText(curDish.salePercent.toString())
        description_textView.setText(curDish.description)
    }

    private fun updateDish(curDish: Dish) {
        try {
            val newPriceS = priceS_textView.text.toString().toDouble()
            val newPriceM = priceM_textView.text.toString().toDouble()
            val newPriceL = priceL_textView.text.toString().toDouble()

            val newSaleOff = saleOff_textView.text.toString().toLong()
            val newDes = description_textView.text.toString()

            val dbRef = FirebaseDatabase.getInstance().getReference("Product/${curDish.id}")

            dbRef.child("priceS").setValue(newPriceS)
            dbRef.child("priceM").setValue(newPriceM)
            dbRef.child("priceL").setValue(newPriceL)

            dbRef.child("salePercent").setValue(newSaleOff)
            dbRef.child("description").setValue(newDes)

            disableEdit()
            Toast.makeText(this, "Update successfully!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Please full fill information", Toast.LENGTH_LONG).show()
        }

    }

    private fun enableEdit() {
        foodName_text.isEnabled = true
        priceS_textView.isEnabled = true
        priceM_textView.isEnabled = true
        priceL_textView.isEnabled = true
        saleOff_textView.isEnabled = true
        description_textView.isEnabled = true
        btnUpdate.visibility = View.VISIBLE
    }

    private fun disableEdit() {
        foodName_text.isEnabled = false
        priceS_textView.isEnabled = false
        priceM_textView.isEnabled = false
        priceL_textView.isEnabled = false
        saleOff_textView.isEnabled = false
        description_textView.isEnabled = false
        btnUpdate.visibility = View.GONE
    }
}
