package com.example.orderfoodappadmin.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.orderfoodappadmin.MainActivity
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.model.Dish
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class AddFoodActivity : AppCompatActivity() {
    private var providerID = ""
    private lateinit var imageUri: Uri

    //Element UI
    lateinit var navView_add_food: NavigationView
    lateinit var menu_button_add_food: LinearLayout
    lateinit var drawerLayout_add_food: DrawerLayout
    lateinit var type_of_food_option: Spinner
    lateinit var browse_button: Button
    lateinit var add_button: Button
    lateinit var clear_button: Button
    lateinit var food_name_editText: EditText
    lateinit var sale_editText: EditText
    lateinit var price_s_size: EditText
    lateinit var price_m_size: EditText
    lateinit var price_l_size: EditText
    lateinit var amount_s_size: EditText
    lateinit var amount_m_size: EditText
    lateinit var amount_l_size: EditText
    lateinit var image_food: ImageView
    lateinit var description_edit_text: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        navView_add_food = findViewById(R.id.navView_add_food)
        menu_button_add_food = findViewById(R.id.menu_button_add_food)
        drawerLayout_add_food = findViewById(R.id.drawerLayout_add_food)
        type_of_food_option = findViewById(R.id.type_of_food_option)
        browse_button = findViewById(R.id.browse_button)
        add_button = findViewById(R.id.add_button)
        clear_button = findViewById(R.id.clear_button)
        food_name_editText = findViewById(R.id.food_name_editText)
        sale_editText = findViewById(R.id.sale_editText)
        price_s_size = findViewById(R.id.price_s_size)
        price_m_size = findViewById(R.id.price_m_size)
        price_l_size = findViewById(R.id.price_l_size)
        amount_s_size = findViewById(R.id.amount_s_size)
        amount_m_size = findViewById(R.id.amount_m_size)
        amount_l_size = findViewById(R.id.amount_l_size)
        image_food = findViewById(R.id.image_food)
        description_edit_text = findViewById(R.id.description_edit_text)

        navView_add_food.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page -> startActivity(Intent(this, ProfileActivity::class.java))
//                R.id.edit_profile -> startActivity(Intent(this, EditProfileActivity::class.java))
                R.id.sign_out -> {
                    Firebase.auth.signOut()
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    Toast.makeText(applicationContext, "Sign out", Toast.LENGTH_SHORT).show()
                }
//                R.id.statistical -> {
//                    val intent = Intent(Intent(this, AnalyzeActivity::class.java))
//                    startActivity(intent)
//                }
            }
            true
        }

        menu_button_add_food.setOnClickListener {
            drawerLayout_add_food.openDrawer(GravityCompat.START)
        }

        findProviderID()

        //set data for type of food combobox
        val listOption = arrayListOf("All food", "Starchy food", "Drinking", "Asian")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOption)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        type_of_food_option.adapter = adapter


        browse_button.setOnClickListener {
            selectImage()
        }

        clear_button.setOnClickListener {
            clearText()
        }

        add_button.setOnClickListener {
            addFood()
        }
    }

    private fun clearText() {
        food_name_editText.text.clear()
        type_of_food_option.setSelection(-1)
        sale_editText.text.clear()

        price_s_size.text.clear()
        price_m_size.text.clear()
        price_l_size.text.clear()

        amount_s_size.text.clear()
        amount_m_size.text.clear()
        amount_l_size.text.clear()

        image_food.setImageDrawable(null)
        description_edit_text.text.clear()
    }

    private fun addFood() {
        try {
            val name = food_name_editText.text.toString()
            val type = type_of_food_option.selectedItem.toString()
            val sale = sale_editText.text.toString().toLong()

            val priceS = price_s_size.text.toString().toDouble()
            val priceM = price_m_size.text.toString().toDouble()
            val priceL = price_l_size.text.toString().toDouble()

            val amountS = amount_s_size.text.toString().toLong()
            val amountM = amount_m_size.text.toString().toLong()
            val amountL = amount_l_size.text.toString().toLong()

            val description = description_edit_text.text.toString()

            if (name.isEmpty() || type.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please full fill all information", Toast.LENGTH_LONG).show()
            } else {
                val dbRef = FirebaseDatabase.getInstance().getReference("Product")
                val key = dbRef.push().key

                val newDish = Dish(
                    key!!,
                    name,
                    priceS,
                    priceM,
                    priceL,
                    "0",
                    type,
                    description,
                    sale,
                    amountS,
                    0L,
                    amountM,
                    0L,
                    amountL,
                    0L,
                    providerID
                )


                dbRef.child(key).setValue(newDish)
                uploadImage(key)
                Toast.makeText(this, "Add to menu successfully!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error, please check all information again!", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun uploadImage(id: String) {
        val storageRef = FirebaseStorage.getInstance().getReference("dish_image/$id.jpg")
        storageRef.putFile(imageUri)
    }

    private fun selectImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            image_food.setImageURI(imageUri)
        }
    }
    private fun findProviderID() {
        val providerEmail = Firebase.auth.currentUser?.email.toString()
        val dbRef = FirebaseDatabase.getInstance()
            .getReference("Provider")
            .orderByChild("email")
            .equalTo(providerEmail)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    providerID = data.key.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}