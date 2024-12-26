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
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private  lateinit var bottomNavBar: ChipNavigationBar
    private lateinit var containerFragment: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bottomNavBar = binding.bottomNavBar
        containerFragment = binding.frameContainerFragment
        initializeChipNavigationBar()
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
}