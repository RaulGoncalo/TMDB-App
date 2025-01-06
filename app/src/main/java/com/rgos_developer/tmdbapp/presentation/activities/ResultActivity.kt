package com.rgos_developer.tmdbapp.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.databinding.ActivityResultBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.presentation.adapters.MovieItemAdapter
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import com.rgos_developer.tmdbapp.presentation.viewModels.MovieViewModel
import com.rgos_developer.tmdbapp.utils.GeneralConstants
import com.rgos_developer.tmdbapp.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    private lateinit var movieViewModel: MovieViewModel
    private var searchResultItemAdapter: MovieItemAdapter? = null

    private var search: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        search = intent.getStringExtra(GeneralConstants.PUT_EXTRAS_SEARCH)

        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        initViews()

        setupObservers()

        if (search != null) {
            movieViewModel.getSearchMovie(search!!)
        } else {
            showMessage("Erro com o parâmetro de busca!")
        }
    }

    private fun setupObservers() {
        movieViewModel.searchMovie.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> binding.pbResult.visibility = View.VISIBLE
                is ResultState.Success -> {
                    searchResultItemAdapter?.loadList(state.value)
                    binding.pbResult.visibility = View.GONE
                }

                is ResultState.Error -> {
                    binding.pbResult.visibility = View.GONE
                    showMessage(state.exception.message.toString())
                }
            }
        }
    }

    private fun initViews() {
        searchResultItemAdapter = MovieItemAdapter() { movie ->
            openMovieDetailActivity(movie)
        }

        binding.rvMovieResults.apply {
            adapter = searchResultItemAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        binding.tvSearchResultTitle.text = "Resultados para: [$search]"
    }

    private fun openMovieDetailActivity(movie: MoviePresentationModel) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(GeneralConstants.PUT_EXTRAS_MOVIE, movie)
        startActivity(intent)
    }
}