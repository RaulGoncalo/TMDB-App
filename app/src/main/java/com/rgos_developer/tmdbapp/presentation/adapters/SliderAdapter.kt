package com.rgos_developer.tmdbapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rgos_developer.tmdbapp.data.dto.MovieDTO
import com.rgos_developer.tmdbapp.databinding.ViewholderSliderItemBinding
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentatioModel

class SliderAdapter(
    private val viewPager2: ViewPager2,
    private val onClickItem: (Long) -> Unit
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    val sliderItems = mutableListOf<MoviePresentatioModel>()

    private var context: Context? = null

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

    fun loadList(newListMovies: List<MoviePresentatioModel>){
        sliderItems.addAll(newListMovies)
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(private val binding: ViewholderSliderItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: MoviePresentatioModel) {
            binding.titleText.setText(sliderItem.title)
            val resquestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))
            context?.let {
                Glide
                    .with(it)
                    .load("https://image.tmdb.org/t/p/original/"+ sliderItem.backdropPath)
                    .apply(resquestOptions)
                    .into(binding.imageSlide)
            }

            binding.root.setOnClickListener {
                onClickItem(sliderItem.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
        val binding: ViewholderSliderItemBinding = ViewholderSliderItemBinding.inflate(view, parent, false)

        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = sliderItems.size
}