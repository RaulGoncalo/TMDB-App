package com.rgos_developer.tmdbapp.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.rgos_developer.tmdbapp.databinding.FragmentFavoriteBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.presentation.activities.MovieDetailsActivity
import com.rgos_developer.tmdbapp.presentation.adapters.MovieItemAdapter
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import com.rgos_developer.tmdbapp.presentation.viewModels.AuthViewModel
import com.rgos_developer.tmdbapp.presentation.viewModels.UserViewModel
import com.rgos_developer.tmdbapp.utils.GeneralConstants
import com.rgos_developer.tmdbapp.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel

    private var userId: String? = null

    private var moviesItemAdapter: MovieItemAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        initViews()
        setupObservers()
        authViewModel.getCurrentUserId()

        return binding.root
    }



    private fun openMovieDetailActivity(movie: MoviePresentationModel) {
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra(GeneralConstants.PUT_EXTRAS_MOVIE, movie)
        startActivity(intent)
    }

    private fun initViews() {
        with(binding){
            moviesItemAdapter = MovieItemAdapter{movie ->
                openMovieDetailActivity(movie)
            }

            rvFavoritesMovies.apply {
                adapter = moviesItemAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

    private fun setupObservers() {
        userViewModel.listFavoritesMovies.observe(viewLifecycleOwner) {state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    state.value.forEach {
                        Log.i("testeListarFavoritos", "Filme: ${it.title}")
                        moviesItemAdapter?.loadList(state.value)
                    }
                    hideLoading()
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }

        authViewModel.getCurrentUserId.observe(viewLifecycleOwner){state ->
            when(state){
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    userId = state.value
                    getFavoritesMovies()
                    hideLoading()
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }

        }
    }

    private fun getFavoritesMovies() {
        if (userId != null) {
            userViewModel.getFavoritesMovies(userId!!)
        } else {
            showMessage("Erro ao buscar id do Usuário!")
        }
    }

    private fun hideLoading() {
        binding.pbFavorites.visibility = View.GONE
    }

    private fun showLoading() {
        binding.pbFavorites.visibility = View.VISIBLE
    }
}