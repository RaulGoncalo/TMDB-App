package com.rgos_developer.tmdbapp.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rgos_developer.tmdbapp.data.dto.User
import com.rgos_developer.tmdbapp.utils.GeneralConstants
import com.rgos_developer.tmdbapp.utils.MainConstants
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.presentation.activities.MovieDetailActivity
import com.rgos_developer.tmdbapp.databinding.FragmentHomeBinding
import com.rgos_developer.tmdbapp.presentation.adapters.PopularMoviesItemAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.SliderAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.UpcomingMoviesItemAdapter
import com.rgos_developer.tmdbapp.presentation.viewModels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(){
    //instsance binding
    private lateinit var binding: FragmentHomeBinding
    //instsances firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    //instsances slider
    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPagerSlider.currentItem = binding.viewPagerSlider.currentItem + 1
    }
    //instsances adapters
    private var popularMoviesItemAdapter: PopularMoviesItemAdapter? = null
    private var upcomingMoviesItemAdapter: UpcomingMoviesItemAdapter? = null
    private var sliderAdapter: SliderAdapter? = null
    //user
    private var user: User? = null
    //ViewModel
    private lateinit var movieViewModel: MovieViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore =  FirebaseFirestore.getInstance()

        //incializações de views e banners
        initViews()
        initBanner()

        //ViewModel//
        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        //busca de dados
        getUser()
        loadMoviesData()

        return binding.root
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

    private fun getUser() : User {
        val idUser = firebaseAuth.currentUser?.uid

        val user = User()
        if(idUser != null){
            firebaseFirestore
                .collection("users")
                .document(idUser)
                .get()
                .addOnSuccessListener {document ->
                    val data = document.data
                    if(data != null){
                        val name = data["name"] as String
                        val email = data["email"] as String
                        val photo = data["photo"] as String

                        user.id = idUser
                        user.name = name
                        user.email = email
                        user.photo = photo



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
                    }
                }
        }

        return user
    }

    private fun loadMoviesData() {
        // Observar mudanças no estado de carregamento
        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoading(MainConstants.TYPE_POPULAR)
                showLoading(MainConstants.TYPE_UPCOMING)
                showLoading(MainConstants.TYPE_TOP_RATED)
            } else {
                hideLoading(MainConstants.TYPE_POPULAR)
                hideLoading(MainConstants.TYPE_UPCOMING)
                hideLoading(MainConstants.TYPE_TOP_RATED)
            }
        }
        movieViewModel.listPopularMovies.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                popularMoviesItemAdapter?.loadList(it)
            }else{
                showMessage("Erro ao buscar filmes populares!")
            }
        }

        movieViewModel.listUpcomingMovies.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                upcomingMoviesItemAdapter?.loadList(it)
            }else{
                showMessage("Erro ao buscar filmes que estão por vim!")
            }
        }

        movieViewModel.listTopRatedMovies.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                sliderAdapter?.loadList(it)
            }else{
                showMessage("Erro ao buscar filmes mais votados!")
            }
        }
    }

    private fun openMovieDetailActivity(idMovie: Long) {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtra(GeneralConstants.PUT_EXTRAS_ID_MOVIE, idMovie)
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

    private fun showLoading(type: String) {
        when (type) {
            MainConstants.TYPE_POPULAR -> binding.pbPopularMovies.visibility = View.VISIBLE
            MainConstants.TYPE_UPCOMING -> binding.pbUpcomingMovies.visibility = View.VISIBLE
            MainConstants.TYPE_TOP_RATED -> binding.progressBarSlider.visibility = View.VISIBLE
        }
    }

    private fun hideLoading(type: String) {
        when (type) {
            MainConstants.TYPE_POPULAR -> binding.pbPopularMovies.visibility = View.GONE
            MainConstants.TYPE_UPCOMING -> binding.pbUpcomingMovies.visibility = View.GONE
            MainConstants.TYPE_TOP_RATED -> binding.progressBarSlider.visibility = View.GONE
        }
    }

    private fun initViews() {
        with(binding) {
            popularMoviesItemAdapter = PopularMoviesItemAdapter() { idMovie ->
                openMovieDetailActivity(idMovie)
            }
            upcomingMoviesItemAdapter = UpcomingMoviesItemAdapter() { idMovie ->
                openMovieDetailActivity(idMovie)
            }

            rvPopularMovies.apply {
                adapter = popularMoviesItemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            rvUpcomingMovies.apply {
                adapter = upcomingMoviesItemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}