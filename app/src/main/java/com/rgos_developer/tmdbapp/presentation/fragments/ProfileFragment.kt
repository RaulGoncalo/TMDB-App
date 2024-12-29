package com.rgos_developer.tmdbapp.presentation.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.presentation.activities.SignInActivity
import com.rgos_developer.tmdbapp.presentation.BaseView
import com.rgos_developer.tmdbapp.databinding.FragmentProfileBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    //DataBinding
    private lateinit var binding: FragmentProfileBinding
    //Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    //User data
    private var idUser: String? = null
    private var uriLocalImage: Uri? = null
    private var currentUser: User? = null
    //Permissons
    private var isThereCameraPermission = false
    private var isThereGalleryPermission = false

    private val galleryManager = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){uri ->
        if(uri != null){
            binding.imageProfile.setImageURI(uri)
            uriLocalImage = uri
        }else{
            showMessage("Nenhuma imagem selecionada")
        }
    }

    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        idUser = firebaseAuth.currentUser?.uid
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupObservers()

        getPermissions()
        initViews()

        getUserData(idUser)
        return binding.root
    }

    private fun setupObservers() {
        userViewModel.getUserState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> setDisplayUser(state.value)
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }

        userViewModel.updateUserState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    hideLoading()
                    showMessage(state.value)
                }
                is ResultState.Error -> showMessage(state.exception.message.toString())
            }
        }
    }

    private fun setDisplayUser(user: User) {
        currentUser = user
        binding.editTextNameProfile.setText(user.name)
        if(user.photo.isNotEmpty()){
            Glide
                .with(this)
                .load(user.photo)
                .into(binding.imageProfile)
        }
        hideLoading()
    }

    private fun updateDataProfile() {
        val newName = binding.editTextNameProfile.text.toString()
        if(newName.isNotEmpty()){
            if(currentUser != null){
                val newUser = User(
                    id = currentUser!!.id,
                    email = currentUser!!.email,
                    name = newName,
                    photo = currentUser!!.photo
                )

                userViewModel.updateUserData(newUser, uriLocalImage)
            }else{
                showMessage("Erro ao buscar usuário atual")
            }
        }else{
            showMessage("Preencha seu nome")
        }
    }

    private fun getUserData(idUser: String?) {
        if(idUser != null){
            userViewModel.getUserData(idUser)
        }
    }

    fun initViews() {
        binding.imageProfile.setOnClickListener {
            if(isThereGalleryPermission){
                galleryManager.launch("image/*")
            }else{
                showMessage("Nenhuma imagem selecionada")
                getPermissions()
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateDataProfile()
        }

        binding.btnSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Deslogar")
                .setMessage("Deseja realmente sair?")
                .setNegativeButton("Cancelar"){dialog, posicao -> }
                .setPositiveButton("Sim") {dialog, posicao ->
                    firebaseAuth.signOut()
                    // Navega para a SignInActivity
                    val intent = Intent(requireContext(), SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    requireActivity().finish()
                }
                .create()
                .show()
        }
    }

    private fun getPermissions() {
        //verificar se o usuario já tem essas permissões
        isThereCameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        isThereGalleryPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        //Lista de permissões negadas

        val listPermissionsDenied = mutableListOf<String>()

        if(!isThereCameraPermission){
            listPermissionsDenied.add(Manifest.permission.CAMERA)
        }
        if(!isThereGalleryPermission){
            listPermissionsDenied.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        if(listPermissionsDenied.isNotEmpty()){
            //Solicitar ao usuario apenas a lista das permissões negadas
            val gerenciadorPermissoes = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ){permissoes ->
                isThereCameraPermission = permissoes[Manifest.permission.CAMERA] ?: isThereCameraPermission
                isThereGalleryPermission = permissoes[Manifest.permission.READ_MEDIA_IMAGES] ?: isThereGalleryPermission

                if(isThereCameraPermission){
                    listPermissionsDenied.remove(Manifest.permission.CAMERA)
                }

                if(isThereGalleryPermission){
                    listPermissionsDenied.remove(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }

            gerenciadorPermissoes.launch(listPermissionsDenied.toTypedArray())
        }
    }

    private fun showLoading() {
        binding.pbProfile.visibility = View.VISIBLE
        binding.btnUpdateProfile.isEnabled = false
        binding.imageProfile.isEnabled = false
    }

    private fun hideLoading() {
        binding.pbProfile.visibility = View.GONE
        binding.btnUpdateProfile.isEnabled = true
        binding.imageProfile.isEnabled = true
    }
}