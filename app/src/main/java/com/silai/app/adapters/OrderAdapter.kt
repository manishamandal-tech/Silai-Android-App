package com.silai.app.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silai.app.databinding.ItemOrderBinding
import com.silai.app.models.Order

class OrderAdapter(private val orders: MutableList<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val b = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(b)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        with(holder.binding) {
            tvOrderId.text = "Order #${order.id}"
            tvTailorName.text = order.tailorName
            tvClothType.text = order.clothType
            tvOrderDate.text = "📅 ${order.date} at ${order.time}"
            tvOrderStatus.text = order.status
            // Color-code status
            tvOrderStatus.setTextColor(when(order.status) {
                "Pickup Scheduled" -> Color.parseColor("#FF9800")
                "Stitching in Progress" -> Color.parseColor("#2196F3")
                "Out for Delivery" -> Color.parseColor("#FF5722")
                "Delivered" -> Color.parseColor("#4CAF50")
                else -> Color.GRAY
            })
            // Step indicators
            step1.alpha = 1f
            step2.alpha = if(order.status in listOf("Stitching in Progress","Out for Delivery","Delivered")) 1f else 0.3f
            step3.alpha = if(order.status in listOf("Out for Delivery","Delivered")) 1f else 0.3f
            step4.alpha = if(order.status == "Delivered") 1f else 0.3f
        }
    }

    override fun getItemCount() = orders.size
    fun updateData(list: List<Order>) { orders.clear(); orders.addAll(list); notifyDataSetChanged() }
}