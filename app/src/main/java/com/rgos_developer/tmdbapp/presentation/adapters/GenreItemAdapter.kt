package com.rgos_developer.tmdbapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rgos_developer.tmdbapp.databinding.ViewholderGenreItemBinding
import com.rgos_developer.tmdbapp.presentation.models.GenrePresentationModel

class GenreItemAdapter: Adapter<GenreItemAdapter.GenreItemViewHolder>() {
    private val listGenre: MutableList<GenrePresentationModel> = mutableListOf<GenrePresentationModel>()

    fun addListGenres(newListGenre: List<GenrePresentationModel>){
        listGenre.addAll(newListGenre)
        Log.i("teste_GenreItemAdapter", "addListGenres: ${listGenre[0].name}")
        notifyDataSetChanged()
    }

    inner class GenreItemViewHolder(val binding: ViewholderGenreItemBinding) : ViewHolder(binding.root){

        fun bind(genre: GenrePresentationModel){
            binding.textGenre.setText(genre.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderGenreItemBinding.inflate(inflater, parent, false)

        return GenreItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreItemViewHolder, position: Int) {
        holder.bind(listGenre[position])
    }

    override fun getItemCount(): Int = listGenre.size

}