package com.rgos_developer.tmdbapp.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.utils.GeneralConstants
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.presentation.activities.MovieDetailsActivity
import com.rgos_developer.tmdbapp.databinding.FragmentHomeBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.presentation.activities.ResultActivity
import com.rgos_developer.tmdbapp.presentation.adapters.MovieItemAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.SliderAdapter
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import com.rgos_developer.tmdbapp.presentation.viewModels.AuthViewModel
import com.rgos_developer.tmdbapp.presentation.viewModels.MovieViewModel
import com.rgos_developer.tmdbapp.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding

    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPagerSlider.currentItem = binding.viewPagerSlider.currentItem + 1
    }

    private var popularMoviesItemAdapter: MovieItemAdapter? = null
    private var upcomingMoviesItemAdapter: MovieItemAdapter? = null
    private var sliderAdapter: SliderAdapter? = null

    var userId: String? = null

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViews()
        initBanner()

        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupMovieDataObservers()
        setupUserObservers()

        authViewModel.getCurrentUserId()
        movieViewModel.getMovies()

        return binding.root
    }

    private fun setupMovieDataObservers() {
        movieViewModel.listPopularMovies.observe(viewLifecycleOwner){state ->
            when(state){
                is ResultState.Loading -> binding.pbPopularMovies.visibility = View.VISIBLE
                is ResultState.Success -> {
                    popularMoviesItemAdapter?.loadList(state.value)
                    binding.pbPopularMovies.visibility = View.GONE
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }

        movieViewModel.listUpcomingMovies.observe(viewLifecycleOwner){state ->
            when(state){
                is ResultState.Loading -> binding.pbUpcomingMovies.visibility = View.VISIBLE
                is ResultState.Success -> {
                    upcomingMoviesItemAdapter?.loadList(state.value)
                    binding.pbUpcomingMovies.visibility = View.GONE
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }

        movieViewModel.listTopRatedMovies.observe(viewLifecycleOwner){state ->
            when(state){
                is ResultState.Loading -> binding.progressBarSlider.visibility = View.VISIBLE
                is ResultState.Success -> {
                    sliderAdapter?.loadList(state.value)
                    binding.progressBarSlider.visibility = View.GONE
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }
    }

    private fun setupUserObservers() {
        userViewModel.getUserState.observe(viewLifecycleOwner){ state ->
            when (state) {
                is ResultState.Loading -> showLoadingUserInformation()
                is ResultState.Success -> displayUser(state.value)
                is ResultState.Error -> {
                    handleError(state.exception)
                }
            }
        }

        authViewModel.getCurrentUserId.observe(viewLifecycleOwner){state ->
            when(state){
                is ResultState.Loading -> showLoadingUserInformation()
                is ResultState.Success -> {
                    userId = state.value
                    getUserData()
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }
    }

    private fun handleError(error: Exception) {
        binding.progressBarUser.visibility = View.GONE
        binding.textNameUser.text = "Erro ao buscar dados do usuário"
        binding.textEmailUser.text = "Erro ao buscar dados do usuário"
        showMessage(error.message.toString())
    }

    private fun showLoadingUserInformation() {
        binding.progressBarUser.visibility = View.VISIBLE
    }

    private fun displayUser(data: User) {
        val name = data.name
        val email = data.email
        val photo = data.photo

        binding.textNameUser.text = name
        binding.textEmailUser.text = email


        if(photo.isNotEmpty()){
            context?.let {
                Glide
                    .with(it)
                    .load(photo)
                    .into(binding.imageProfile)
            }
        }
        binding.progressBarUser.visibility = View.GONE
    }

    private fun getUserData() {
        if(userId != null){
            userViewModel.getUserData(userId!!)
        }
    }

    private fun openMovieDetailActivity(movie: MoviePresentationModel) {
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra(GeneralConstants.PUT_EXTRAS_MOVIE, movie)
        startActivity(intent)
    }

    private fun initBanner() {
        // Inicialize o adapter e atribua ao ViewPager2
        sliderAdapter = SliderAdapter(binding.viewPagerSlider) { idMovie ->
            openMovieDetailActivity(idMovie)
        }
        binding.viewPagerSlider.adapter = sliderAdapter
        binding.viewPagerSlider.clipToPadding = false
        binding.viewPagerSlider.clipChildren = false
        binding.viewPagerSlider.offscreenPageLimit = 3
        binding.viewPagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer(ViewPager2.PageTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85F + r * 0.15F
            })
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
        binding.viewPagerSlider.currentItem = 1
        // Configuração do comportamento do slider automático
        binding.viewPagerSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })
    }

    private fun initViews() {
        with(binding) {
            popularMoviesItemAdapter = MovieItemAdapter() { movie ->
                openMovieDetailActivity(movie)
            }

            upcomingMoviesItemAdapter = MovieItemAdapter() { movie ->
                openMovieDetailActivity(movie)
            }

            rvPopularMovies.apply {
                adapter = popularMoviesItemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            rvUpcomingMovies.apply {
                adapter = upcomingMoviesItemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            etSearch.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // Verifica se o clique foi no drawableEnd
                    val drawableEnd = etSearch.compoundDrawablesRelative[2] // Drawable do lado direito
                    if (drawableEnd != null) {
                        val drawableWidth = drawableEnd.bounds.width()
                        val clickAreaStart = etSearch.width - etSearch.paddingEnd - drawableWidth
                        if (event.x > clickAreaStart) {
                            // Clique detectado no ícone drawableEnd
                            goToResultActivity()
                            return@setOnTouchListener true
                        }
                    }
                }
                false
            }
        }
    }

    private fun goToResultActivity() {
        val textSearch = binding.etSearch.text.toString()
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra(GeneralConstants.PUT_EXTRAS_SEARCH, textSearch)

        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderHandler.removeCallbacks(sliderRunnable)
    }
}