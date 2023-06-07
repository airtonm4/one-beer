package com.example.onebeer.Profile

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.MainActivity

import com.example.onebeer.databinding.ProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ProfilePage: Fragment() {

    private var _binding: ProfilePageBinding? = null

    private val binding get() = _binding!!

     private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

//        val db = Firebase.firestore
//        val beer = hashMapOf(
//            "title" to "Olaria",
//            "ml" to 800,
//            "price" to 42,
//            "style" to "APA Pale Ale",
//            "description" to "Cerveja originária de Senador Canedo - GO"
//
//        )
//
//        db.collection("beers")
//            .add(beer)
//            .addOnSuccessListener { documentReference ->
//                Log.d("Beer add", "DocumentSnapshot added with ID: ${documentReference.id}")
//                Log.d("Beer add", documentReference.toString())
//            }.addOnFailureListener { exception ->
//                Log.d("failure", exception.toString())
//            }

        val storage = Firebase.storage.reference

        auth = Firebase.auth
        val user = auth.currentUser
        _binding = ProfilePageBinding.inflate(inflater, container, false)

        if (user != null) {
            binding.textviewProfile.text = user.displayName
        }

        binding.butaoLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, MainActivity::class.java))
        }
        return binding.root
    }

}