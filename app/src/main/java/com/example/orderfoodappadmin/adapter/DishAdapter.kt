package com.example.orderfoodappadmin.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.activity.FoodDetail
import com.example.orderfoodappadmin.model.Dish
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.food_item.view.*
import java.io.File
import java.text.DecimalFormat


class DishAdapter (
    private val dishList: MutableList<Dish>
): RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    class DishViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        return DishViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.food_item,
                parent,
                false
            )
        )
    }

    fun addDish(dish: Dish) {
        var sum: Long = 0
        var count = 0
        val dbRef = FirebaseDatabase.getInstance().getReference("Comment/${dish.id}")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children) {
                    sum += data.child("rating").value as Long
                    count++
                }

                if(count != 0) {
                    val rating: Double = sum*1.0 / count
                    val df = DecimalFormat("#.#")
                    dish.rated = df.format(rating)
                }
                else {
                    dish.rated = "0"
                }

                dishList.add(dish)
                notifyItemInserted(dishList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun deleteAll() {
        dishList.clear()
        notifyDataSetChanged()
    }

    private fun deleteItem(pos: Int) {
        dishList.removeAt(pos)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val curDish = dishList[position]

        val storageRef = FirebaseStorage.getInstance().getReference("dish_image/${curDish.id}.jpg")
        try {
            val localFile = File.createTempFile("tempfile", ".jpg")
            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                holder.itemView.foodImage_imageView.setImageBitmap(bitmap)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        holder.itemView.apply {
            foodName_textView.text = curDish.name
            star_rating_text.text = curDish.rated
            priceS_value.text = "$" + curDish.priceS.toString()
            priceM_value.text = "$" + curDish.priceM.toString()
            priceL_value.text = "$" +  curDish.priceL.toString()

//            delete_button.setOnClickListener {
//                deleteItem(position)
//                deleteDish(curDish)
//
//            }

            foodImage_imageView.setOnClickListener {
                val intent = Intent(context, FoodDetail::class.java)
                intent.putExtra("curDish", curDish)
                context.startActivities(arrayOf(intent))
            }
        }

    }

    private fun deleteDish(curDish: Dish) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Product/${curDish.id}")
        dbRef.ref.removeValue()
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

}
