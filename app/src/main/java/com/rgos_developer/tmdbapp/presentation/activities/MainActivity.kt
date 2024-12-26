package com.rgos_developer.tmdbapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.rgos_developer.tmdbapp.databinding.ActivityMainBinding
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.presentation.fragments.FavoriteFragment
import com.rgos_developer.tmdbapp.presentation.fragments.HomeFragment
import com.rgos_developer.tmdbapp.presentation.fragments.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //-----------------Atributos--------------------------
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private  lateinit var bottomNavBar: ChipNavigationBar

    private lateinit var containerFragment: FrameLayout

    //Utilizar o viewPager para dislizar entres as fragments utilizando o ChipNavigationBar
    /*
    private lateinit var viewPagerAdapter: ContainerFragmentAdapter
    private lateinit var containerFragment: ViewPager2
    */

    //-----------------Métodos--------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bottomNavBar = binding.bottomNavBar
        containerFragment = binding.frameContainerFragment
        initializeChipNavigationBar()

        //Utilizar o viewPager para dislizar entres as fragments utilizando o ChipNavigationBar
        /*
        containerFragment = binding.containerFragment
        setupViewPager()
        setupBottomNavigation()
        */
    }

    private fun initializeChipNavigationBar() {
        binding.bottomNavBar.setOnItemSelectedListener {menuItem ->
            when(menuItem){
                R.id.explorer -> {
                    replaceFragments(HomeFragment())
                    true
                }
                R.id.favorites -> {
                    replaceFragments(FavoriteFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragments(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        binding.bottomNavBar.setItemSelected(R.id.favorites, true)
        replaceFragments(FavoriteFragment())
    }

    private fun replaceFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameContainerFragment, fragment).commit()
    }

    //Utilizar o viewPager para dislizar entres as fragments utilizando o ChipNavigationBar
    /*private fun setupViewPager() {
        viewPagerAdapter = ContainerFragmentAdapter(
            supportFragmentManager,
            lifecycle
        )
        containerFragment.adapter = viewPagerAdapter

        containerFragment.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        bottomNavBar.setItemSelected(R.id.explorer, true)
                    }
                    1 -> {
                        bottomNavBar.setItemSelected(R.id.favorites, true)
                    }
                    2 -> {
                        bottomNavBar.setItemSelected(R.id.profile, true)
                    }
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem) {
                R.id.explorer -> {
                    containerFragment.currentItem = 0
                    true
                }
                R.id.favorites -> {
                    containerFragment.currentItem = 1
                    true
                }
                R.id.profile -> {
                    containerFragment.currentItem = 2
                    true
                }
                else -> false
            }
        }

        // Define o item inicial
        bottomNavBar.setItemSelected(R.id.explorer, true)
    }*/
}