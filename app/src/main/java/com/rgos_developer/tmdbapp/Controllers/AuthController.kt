package com.rgos_developer.tmdbapp.Controllers

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rgos_developer.tmdbapp.data.dto.User

class AuthController (val view: Activity){

    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getUser(){
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
                    }
                }
        }
    }
}