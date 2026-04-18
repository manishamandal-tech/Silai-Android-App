package com.silai.app.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silai.app.databinding.ItemAdminOrderBinding
import com.silai.app.models.Order

class AdminOrderAdapter(private val orders: MutableList<Order>, val onUpdate: (Order) -> Unit) :
    RecyclerView.Adapter<AdminOrderAdapter.VH>() {

    inner class VH(val b: ItemAdminOrderBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val o = orders[position]
        holder.b.tvOrderId.text = "Order #${o.id}"
        holder.b.tvUserTailor.text = "User ${o.userId} → ${o.tailorName}"
        holder.b.tvClothDate.text = "${o.clothType} | ${o.date}"
        holder.b.tvStatus.text = o.status
        holder.b.btnUpdateStatus.setOnClickListener { onUpdate(o) }
    }

    override fun getItemCount() = orders.size
    fun updateData(list: List<Order>) { orders.clear(); orders.addAll(list); notifyDataSetChanged() }
}