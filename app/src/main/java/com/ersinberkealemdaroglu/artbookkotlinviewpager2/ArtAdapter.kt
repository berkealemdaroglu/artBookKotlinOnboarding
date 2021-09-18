package com.ersinberkealemdaroglu.artbookkotlinviewpager2

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.databinding.ReceylerRowBinding

class ArtAdapter (val artList : ArrayList<ArtManager>) : RecyclerView.Adapter<ArtAdapter.ArtHolderAdapter>() {

    class ArtHolderAdapter (val binding: ReceylerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolderAdapter {

        val binding = ReceylerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtHolderAdapter(binding)
    }

    override fun onBindViewHolder(holder: ArtHolderAdapter, position: Int) {
        holder.binding.receylerRowTextView.text = artList.get(position).artnames
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,ArtActivity::class.java)
            intent.putExtra("infos","old")
            intent.putExtra("id",artList.get(position).id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return artList.size
    }
}