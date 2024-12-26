package com.rgos_developer.tmdbapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.presentation.adapters.CastItemAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.GenreItemAdapter
import com.rgos_developer.tmdbapp.utils.GeneralConstants
import com.rgos_developer.tmdbapp.utils.MovieDetailConstants
import com.rgos_developer.tmdbapp.presentation.BaseView
import com.rgos_developer.tmdbapp.databinding.ActivityMovieDetailBinding
import com.rgos_developer.tmdbapp.presentation.models.MovieCreditsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MovieDetailsPresentationModel
import com.rgos_developer.tmdbapp.presentation.viewModels.MovieDetailsCreditsViewModel
import com.rgos_developer.tmdbapp.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity(), BaseView {

    private val binding: ActivityMovieDetailBinding by lazy {
        ActivityMovieDetailBinding.inflate(layoutInflater)
    }

    private var idMovie: Long? = null

    private lateinit var viewModel: MovieDetailsCreditsViewModel
    //private val movieDetailConstroller = MovieDetailConstroller(RetrofitClient.moviesApi, this)

    private var genreItemAdapter: GenreItemAdapter? = null
    private var castItemAdapter: CastItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        idMovie = intent.getLongExtra(GeneralConstants.PUT_EXTRAS_ID_MOVIE, 0)

        initViews()

        viewModel = ViewModelProvider(this)[MovieDetailsCreditsViewModel::class.java]

        viewModel.movieDetails.observe(this) { details ->
            if (details != null) {
                displayMovieDetails(details)
            }
        }

        viewModel.movieCredits.observe(this) { credits ->
            if (credits != null) {
                displayMovieCredits(credits)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoading(MovieDetailConstants.TYPE_DETAILS)
                showLoading(MovieDetailConstants.TYPE_CREDITS)
            } else {
                hideLoading(MovieDetailConstants.TYPE_DETAILS)
                hideLoading(MovieDetailConstants.TYPE_CREDITS)
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                showMessage(it)
            }
        }

        // Chama a função para buscar dados (apenas se o ID do filme for válido)
        if (idMovie != null && idMovie != 0L) {
            viewModel.getMovieDetailsCredits(idMovie!!)
        } else {
            showMessage("Erro: ID do filme não encontrado")
        }
    }

    fun displayMovieDetails(movie: MovieDetailsPresentationModel) {
        val resquestOptions =
            RequestOptions().transform(FitCenter(), GranularRoundedCorners(0f, 0f, 50f, 50f))
        Glide
            .with(this@MovieDetailActivity)
            .load("https://image.tmdb.org/t/p/original${movie.posterPath}")
            .apply(resquestOptions)
            .error(R.drawable.profile)
            .into(binding.moviePic)

        binding.textMovieDetailTitle.text = movie.title
        binding.textImdb.text = String.format("%.2f", movie.voteAverage)
        binding.textSummery.text = movie.overview
        binding.textMovieTime.text = movie.runtime.toString() + " minutos"

        if (genreItemAdapter != null) {
            genreItemAdapter!!.addListGenres(movie.genres)
        }
    }

    fun displayMovieCredits(credits: MovieCreditsPresentationModel) {
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
