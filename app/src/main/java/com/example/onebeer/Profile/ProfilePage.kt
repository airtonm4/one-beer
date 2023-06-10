package com.example.onebeer.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onebeer.MainActivity
import com.example.onebeer.databinding.ProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfilePage: Fragment() {

    private var _binding: ProfilePageBinding? = null

    private val binding get() = _binding!!

     private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

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