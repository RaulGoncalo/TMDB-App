package com.rgos_developer.tmdbapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.rgos_developer.tmdbapp.databinding.FragmentFavoriteBinding
import com.rgos_developer.tmdbapp.presentation.viewModels.UserViewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {

    }
}