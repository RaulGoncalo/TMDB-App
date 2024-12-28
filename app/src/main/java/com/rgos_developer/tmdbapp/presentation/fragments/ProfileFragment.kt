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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.presentation.activities.SignInActivity
import com.rgos_developer.tmdbapp.presentation.BaseView
import com.rgos_developer.tmdbapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(), BaseView {
    //DataBinding
    private lateinit var binding: FragmentProfileBinding
    //Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    //User data
    private var idUser: String? = null
    private var updatedData = mutableMapOf<String, String>()
    private var uriLocalImage: Uri? = null
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
            requireActivity().showMessage("Nenhuma imagem selecionada")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        idUser = firebaseAuth.currentUser?.uid
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        getPermissions()
        initViews()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getInitialDataUser()
    }


    private fun updateDataProfile(idUser: String, updatedData: MutableMap<String, String>) {
        showLoading()
        firebaseFirestore
            .collection("users")
            .document(idUser)
            .update(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                requireActivity().showMessage("Sucesso ao atualizar perfil")
                hideLoading()
            }
            .addOnFailureListener {
                requireActivity().showMessage("Erro ao atualizar perfil")
                hideLoading()
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

    private fun getInitialDataUser() {
        if(idUser != null){
            showLoading()

            firebaseFirestore
                .collection("users")
                .document(idUser!!)
                .get()
                .addOnSuccessListener{ documentSnapshot ->
                    val dadosUsuarios = documentSnapshot.data
                    if (dadosUsuarios != null){
                        val name = dadosUsuarios["name"] as String
                        val photo = dadosUsuarios["photo"] as String

                        binding.editTextNameProfile.setText(name)
                        if(photo.isNotEmpty()){
                            Glide
                                .with(this)
                                .load(photo)
                                .into(binding.imageProfile)
                        }

                        hideLoading()
                    }
                }
        }

    }
    private fun uploadFirebaseStorage(uri: Uri) {
        if(idUser != null){
            showLoading()
            firebaseStorage
                .getReference("photos")
                .child("users")
                .child(idUser!!)
                .child("profile.jpg")
                .putFile(uri)
                .addOnSuccessListener {task ->
                    requireActivity().showMessage("Sucesso ao fazer upload da imagem")
                    task
                        .metadata
                        ?.reference
                        ?.downloadUrl
                        ?.addOnSuccessListener { url ->
                            updatedData["photo"] =  url.toString()
                            updateDataProfile(idUser!!, updatedData) // Atualiza perfil só após o upload bem-sucedido
                            hideLoading()
                        }
                }
                .addOnFailureListener {
                    requireActivity().showMessage("Erro ao fazer upload da imagem")
                }


        }
    }

    override fun showLoading(type: String) {
        binding.pbProfile.visibility = View.VISIBLE
        binding.btnUpdateProfile.isEnabled = false
        binding.imageProfile.isEnabled = false
    }

    override fun hideLoading(type: String) {
        binding.pbProfile.visibility = View.GONE
        binding.btnUpdateProfile.isEnabled = true
        binding.imageProfile.isEnabled = true
    }

    override fun initViews() {
        binding.imageProfile.setOnClickListener {
            if(isThereGalleryPermission){
                galleryManager.launch("image/*")
            }else{
                requireActivity().showMessage("Nenhuma imagem selecionada")
                getPermissions()
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            val newName = binding.editTextNameProfile.text.toString()
            if(newName.isNotEmpty()){
                updatedData["name"] = newName

                if(idUser != null){
                    if (uriLocalImage != null) {
                        // Se há uma imagem selecionada, faz o upload primeiro
                        uploadFirebaseStorage(uriLocalImage!!)
                    } else {
                        // Se não há imagem, atualiza apenas o nome
                        updateDataProfile(idUser!!, updatedData)
                    }
                }else{
                    requireActivity().showMessage("Erro ao buscar id do usuário")
                }
            }else{
                requireActivity().showMessage("Preencha seu nome")
            }
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
}