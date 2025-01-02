package com.rgos_developer.tmdbapp.presentation.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.presentation.adapters.CastItemAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.GenreItemAdapter
import com.rgos_developer.tmdbapp.utils.GeneralConstants
import com.rgos_developer.tmdbapp.databinding.ActivityMovieDetailsBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.presentation.models.MovieCreditsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MovieDetailsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import com.rgos_developer.tmdbapp.presentation.viewModels.MovieDetailsCreditsViewModel
import com.rgos_developer.tmdbapp.presentation.viewModels.UserViewModel
import com.rgos_developer.tmdbapp.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private val binding: ActivityMovieDetailsBinding by lazy {
        ActivityMovieDetailsBinding.inflate(layoutInflater)
    }

    //Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    private var userId: String? = null
    private var movie: MoviePresentationModel? = null

    private lateinit var movieViewModel: MovieDetailsCreditsViewModel
    private lateinit var userViewModel: UserViewModel

    private var genreItemAdapter: GenreItemAdapter? = null
    private var castItemAdapter: CastItemAdapter? = null

    private var isFavoriteMovie = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        movie = if(Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(GeneralConstants.PUT_EXTRAS_MOVIE, MoviePresentationModel::class.java)
        }else{
            intent.getParcelableExtra(GeneralConstants.PUT_EXTRAS_MOVIE)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser?.uid

        initViews()

        movieViewModel = ViewModelProvider(this)[MovieDetailsCreditsViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setupMovieDataObservers()
        fetchMovieData()
    }

    private fun fetchMovieData() {
        if (movie?.id != null && movie?.id != 0L && userId != null) {
            movieViewModel.getMovieDetailsCredits(movie!!.id)
            userViewModel.isFavoriteMovie(userId!!,movie!!.id)
        } else {
            showMessage("Erro: ID do filme ou ID do Usuário não encontrado")
        }
    }

    private fun setupMovieDataObservers() {
        movieViewModel.movieDetails.observe(this) { details ->
            details?.let {
                displayMovieDetails(it)
            }
        }

        movieViewModel.movieCredits.observe(this) { credits ->
            credits?.let { displayMovieCredits(it) }
        }

        movieViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        movieViewModel.errorMessage.observe(this) { message ->
            message?.let { showMessage(it) }
        }

        userViewModel.isFavoriteMovie.observe(this) { state ->
            when(state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    isFavoriteMovie = state.value
                    handleDisplayFavorite(state.value)
                    hideLoading()
                }
                is ResultState.Error -> {
                    showMessage(state.exception.message.toString())
                }
            }
        }

        userViewModel.removeFavoriteMovieState.observe(this){state ->
            when(state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    showMessage(state.value)
                    hideLoading()
                }
                is ResultState.Error -> {
                    showMessage(state.exception.message.toString())
                }
            }
        }
    }

    private fun handleDisplayFavorite(isFavorite: Boolean) {
        if (isFavorite){
            binding.btnFavoriteMovie.setImageResource(R.drawable.ic_favorite_24)
        }else{
            binding.btnFavoriteMovie.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }


    private fun displayMovieDetails(movie: MovieDetailsPresentationModel) {
        val resquestOptions =
            RequestOptions().transform(FitCenter(), GranularRoundedCorners(0f, 0f, 50f, 50f))
        Glide
            .with(this@MovieDetailsActivity)
            .load(movie.posterPath)
            .apply(resquestOptions)
            .error(R.drawable.profile)
            .into(binding.moviePic)

        binding.textMovieDetailTitle.text = movie.title
        binding.textImdb.text = movie.voteAverage
        binding.textSummery.text = movie.overview
        binding.textMovieTime.text = movie.runtime

        if (genreItemAdapter != null) {
            genreItemAdapter!!.addListGenres(movie.genres)
        }
    }

    private fun displayMovieCredits(credits: MovieCreditsPresentationModel) {
        castItemAdapter?.addListCast(credits.cast)
    }

    private fun showLoading() {
        binding.loadingOverlayMovieDetails.visibility = View.VISIBLE
        binding.pbMoviePic.visibility = View.VISIBLE
        binding.pbCast.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingOverlayMovieDetails.visibility = View.GONE
        binding.pbMoviePic.visibility = View.GONE
        binding.pbCast.visibility = View.GONE
    }

    private fun initViews() {
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
            handleFavoriteButtonClick()
        }
    }

    private fun handleFavoriteButtonClick() {
        if(userId != null && movie != null){
            if(isFavoriteMovie){
                userViewModel.removeFavoriteMovie(userId!!, movie!!.id)
            }else{
                userViewModel.addFavoriteMovie(userId!!, movie!!)
            }
            userViewModel.isFavoriteMovie(userId!!, movie!!.id)
        }
    }
}
