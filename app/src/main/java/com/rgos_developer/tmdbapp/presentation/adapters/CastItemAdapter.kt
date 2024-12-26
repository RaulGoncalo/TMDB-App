package com.rgos_developer.tmdbapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.databinding.ViewholderCastItemBinding
import com.rgos_developer.tmdbapp.presentation.models.CastPresentationModel

class CastItemAdapter(
    val context: Context
) : Adapter<CastItemAdapter.CastItemViewHolder>() {
    private val listCast: MutableList<CastPresentationModel> = mutableListOf<CastPresentationModel>()

    fun addListCast(newListCast: List<CastPresentationModel>){
        listCast.addAll(newListCast)
        notifyDataSetChanged()
    }


    inner class CastItemViewHolder(val binding: ViewholderCastItemBinding) : ViewHolder(binding.root){
        fun bind(cast: CastPresentationModel){
            binding.textNameActor.setText(cast.name)

            val imageUrl = if (cast.profilePath != null) {
                "https://image.tmdb.org/t/p/original${cast.profilePath}"
            } else {
                null // Indica ao Glide que o caminho é inválido
            }

            Glide
                .with(context)
                .load(imageUrl)
                .error(R.drawable.profile)
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