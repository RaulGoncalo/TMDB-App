package com.rgos_developer.tmdbapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.rgos_developer.tmdbapp.databinding.ViewholderCastItemBinding

class CastItemAdapter(
    val context: Context
) : Adapter<CastItemAdapter.CastItemViewHolder>() {
    private val listCast: MutableList<Cast> = mutableListOf<Cast>()

    fun addListCast(newListCast: List<Cast>){
        listCast.addAll(newListCast)
        notifyDataSetChanged()
    }


    inner class CastItemViewHolder(val binding: ViewholderCastItemBinding) : ViewHolder(binding.root){
        fun bind(cast: Cast){
            binding.textNameActor.setText(cast.name)

            Glide
                .with(context)
                .load("https://image.tmdb.org/t/p/original${cast.profile_path}")
                .into(binding.imageViewProfileActor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderCastItemBinding.inflate(inflater, parent, false)

        return CastItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastItemViewHolder, position: Int) {
        holder.bind(listCast[position])
    }

    override fun getItemCount(): Int = listCast.size

}