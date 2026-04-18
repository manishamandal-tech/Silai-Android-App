package com.silai.app.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.silai.app.R
import com.silai.app.databinding.ItemTailorBinding
import com.silai.app.models.Tailor

// RecyclerView Adapter for tailor list
// VIVA TIP: Adapter = bridge between data list and RecyclerView
// ViewHolder = holds references to views for ONE item (avoids repeated findViewById)
class TailorAdapter(
    private val tailors: MutableList<Tailor>,
    private val onClick: (Tailor) -> Unit
) : RecyclerView.Adapter<TailorAdapter.TailorViewHolder>() {

    inner class TailorViewHolder(val binding: ItemTailorBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Called once per visible item — inflate item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TailorViewHolder {
        val binding = ItemTailorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TailorViewHolder(binding)
    }

    // Called every time an item needs to be displayed — bind data to views
    override fun onBindViewHolder(holder: TailorViewHolder, position: Int) {
        val tailor = tailors[position]
        with(holder.binding) {
            tvTailorName.text = tailor.name
            tvTailorArea.text = "📍 ${tailor.area}"
            tvTailorRating.text = "⭐ ${tailor.rating}"
            ratingBar.rating = tailor.rating.toFloat()
            Glide.with(root.context)
                .load(tailor.imageUrl)
                .placeholder(R.drawable.ic_tailor_placeholder)
                .circleCrop()
                .into(ivTailor)
            root.setOnClickListener { onClick(tailor) }
        }
    }

    override fun getItemCount() = tailors.size

    fun updateData(newList: List<Tailor>) {
        tailors.clear()
        tailors.addAll(newList)
        notifyDataSetChanged()
    }
}