package com.rgos_developer.tmdbapp.presentation.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rgos_developer.tmdbapp.presentation.adapters.CastItemAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.GenreItemAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.ResultCredits
import com.rgos_developer.tmdbapp.Controllers.MovieDetailConstroller
import com.rgos_developer.tmdbapp.data.dto.MovieDetailsDTO
import com.rgos_developer.tmdbapp.data.remote.RetrofitClient
import com.rgos_developer.tmdbapp.Utils.GeneralConstants
import com.rgos_developer.tmdbapp.Utils.MovieDetailConstants
import com.rgos_developer.tmdbapp.presentation.BaseView
import com.rgos_developer.tmdbapp.databinding.ActivityMovieDetailBinding
import eightbitlab.com.blurview.RenderScriptBlur

class MovieDetailActivity : AppCompatActivity(), BaseView {

    private val binding: ActivityMovieDetailBinding by lazy {
        ActivityMovieDetailBinding.inflate(layoutInflater)
    }

    private var idMovie: Long? = null

    private val movieDetailConstroller = MovieDetailConstroller(RetrofitClient.moviesApi, this)

    private var genreItemAdapter: GenreItemAdapter? = null
    private var castItemAdapter: CastItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        idMovie = intent.getLongExtra(GeneralConstants.PUT_EXTRAS_ID_MOVIE, 0)

        initViews()

        if(idMovie != null){
            movieDetailConstroller.getMovieDetails(idMovie!!)
            movieDetailConstroller.getMovieCredits(idMovie!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieDetailConstroller.cancelJobs()
    }


    fun displayMovieDetails(movie: MovieDetailsDTO?) {
        val resquestOptions =
            RequestOptions().transform(FitCenter(), GranularRoundedCorners(0f, 0f, 50f, 50f))
        Glide
            .with(this@MovieDetailActivity)
            .load("https://image.tmdb.org/t/p/original${movie!!.poster_path}")
            .apply(resquestOptions)
            .into(binding.moviePic)

        binding.textMovieDetailTitle.setText(movie!!.title)
        binding.textImdb.setText(String.format("%.2f", movie!!.vote_average))
        binding.textSummery.setText(movie!!.overview)
        binding.textMovieTime.setText(movie!!.runtime.toString() + " minutos")

        if (genreItemAdapter != null) {
            genreItemAdapter!!.addListGenres(movie.genreDTOS)
        }
    }

    fun displayMovieCredits(credits: ResultCredits) {
        castItemAdapter?.addListCast(credits.cast)
    }

    override fun showLoading(type: String) {
        when(type){
            MovieDetailConstants.TYPE_DETAILS -> binding.pbMoviePic.visibility = View.VISIBLE
            MovieDetailConstants.TYPE_CREDITS -> binding.pbCast.visibility = View.VISIBLE
        }
    }

    override fun hideLoading(type: String) {
        when(type){
            MovieDetailConstants.TYPE_DETAILS -> binding.pbMoviePic.visibility = View.GONE
            MovieDetailConstants.TYPE_CREDITS -> binding.pbCast.visibility = View.GONE
        }
    }

    override fun initViews() {
        //RecyclerViews
        genreItemAdapter = GenreItemAdapter()
        binding.rvGenreView.adapter = genreItemAdapter
        binding.rvGenreView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        castItemAdapter = CastItemAdapter(this)
        binding.rvCast.adapter = castItemAdapter
        binding.rvCast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //Blur view config
        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background
        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(radius)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true

        //Botão voltar
        binding.btnBack.setOnClickListener {
            finish()
        }

        //favoritos
        binding.btnFavoriteMovie.setOnClickListener {

        }
    }
}
