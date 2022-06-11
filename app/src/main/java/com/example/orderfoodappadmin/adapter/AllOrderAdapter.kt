package com.example.orderfoodapp.adapters

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfoodappadmin.R
import com.example.orderfoodappadmin.activity.OrderDetail
import com.example.orderfoodappadmin.model.Order
import kotlinx.android.synthetic.main.item_order.view.*


class AllOrderAdapter(
    private val orderList: MutableList<Order>
) : RecyclerView.Adapter<AllOrderAdapter.OrderHisViewHolder>() {

    class OrderHisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHisViewHolder {
        return OrderHisViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_order,
                parent,
                false
            )
        )
    }

    fun addOrder(order: Order) {
        orderList.add(order)
        notifyItemInserted(orderList.size - 1)
    }

    fun deleteAll() {
        orderList.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OrderHisViewHolder, position: Int) {
        val curOrder = orderList[position]
        holder.itemView.apply {
            order_id.text = "#" + curOrder.id
            order_cost_and_amout.text = "$${curOrder.total} • ${curOrder.num} item(s)"
            order_date.text = curOrder.time

            var color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getColor(R.color.baemin)
            } else {
                resources.getColor(R.color.baemin)
            };
            when (curOrder.status) {
                "Pending" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    color = context.getColor(R.color.yellow)
                }
                "Decline" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    color = context.getColor(R.color.red)
                }

                else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    color = context.getColor(R.color.baemin)
                }
            }

            order_id.setTextColor(color)

            setOnClickListener {
                val intent = Intent(context, OrderDetail::class.java)
                intent.putExtra("id", curOrder.id)
                intent.putExtra("status", curOrder.status)
                intent.putExtra("customerEmail", curOrder.customerEmail)
                context.startActivities(arrayOf(intent))
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
