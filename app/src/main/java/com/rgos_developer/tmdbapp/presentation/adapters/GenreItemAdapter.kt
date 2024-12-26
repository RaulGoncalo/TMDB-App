package com.rgos_developer.tmdbapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rgos_developer.tmdbapp.data.dto.GenreDTO
import com.rgos_developer.tmdbapp.databinding.ViewholderGenreItemBinding

class GenreItemAdapter: Adapter<GenreItemAdapter.GenreItemViewHolder>() {
    private val listGenreDTOS: MutableList<GenreDTO> = mutableListOf<GenreDTO>()

    fun addListGenres(newListGenreDTO: List<GenreDTO>){
        listGenreDTOS.addAll(newListGenreDTO)
        Log.i("teste_GenreItemAdapter", "addListGenres: ${listGenreDTOS[0].name}")
        notifyDataSetChanged()
    }

    inner class GenreItemViewHolder(val binding: ViewholderGenreItemBinding) : ViewHolder(binding.root){

        fun bind(genreDTO: GenreDTO){
            binding.textGenre.setText(genreDTO.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderGenreItemBinding.inflate(inflater, parent, false)

        return GenreItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreItemViewHolder, position: Int) {
        holder.bind(listGenreDTOS[position])
    }

    override fun getItemCount(): Int = listGenreDTOS.size

}