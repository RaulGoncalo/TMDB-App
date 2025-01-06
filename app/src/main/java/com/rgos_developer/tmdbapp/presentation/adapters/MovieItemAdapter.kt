package com.rgos_developer.tmdbapp.presentation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rgos_developer.tmdbapp.databinding.ViewholderMovieItemBinding
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import com.rgos_developer.tmdbapp.utils.ApiConstants

class MovieItemAdapter(
    private val onClickItem: (MoviePresentationModel) -> Unit
) : RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>() {

    val listMovies = mutableListOf<MoviePresentationModel>()

    private var context: Context? = null

    fun loadList(newListMovies: List<MoviePresentationModel>) {
        listMovies.clear()
        listMovies.addAll(newListMovies)
        notifyDataSetChanged()
    }

    inner class MovieItemViewHolder(val binding: ViewholderMovieItemBinding) :
        ViewHolder(binding.root) {


        fun bind(movie: MoviePresentationModel) {
            val resquestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(40))
            context?.let {
                Glide
                    .with(it)
                    .load(ApiConstants.BASE_URL_IMAGE_W500 + movie.posterPath)
                    .apply(resquestOptions)
                    .into(binding.pic)
            }

            binding.nameTxt.text = movie.title

            binding.root.setOnClickListener {
                onClickItem(movie)
            }
        }
    }

    //criar view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        context = parent.context

        val inflate = LayoutInflater.from(parent.context)
        val binding = ViewholderMovieItemBinding.inflate(inflate, parent, false)

        return MovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(listMovies[position])
    }


    override fun getItemCount() = listMovies.size
}