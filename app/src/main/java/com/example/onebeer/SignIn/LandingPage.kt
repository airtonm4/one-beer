package com.example.onebeer.SignIn

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onebeer.ConsultantHome.ConsultantHomeActivity
import com.example.onebeer.Home.HomeActivity
import com.example.onebeer.R
import com.example.onebeer.databinding.LandingFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LandingPage : Fragment(){

    private var _binding: LandingFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        val user = auth.currentUser

        /**
         * Auto-login
         */
        if (user != null) {
            val db = Firebase.firestore
            /**
             * Checa se o usuário é admin ou não.
             */
            db.collection("users")
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { userRetrieved ->
                    userRetrieved.forEach { checkUser ->
                        if (checkUser["type"] == "admin"){
                            startActivity(Intent(context, ConsultantHomeActivity::class.java))
                        } else {
                            startActivity(Intent(context, HomeActivity::class.java))
                        }
                    }
                }
        }

        _binding = LandingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignup.setOnClickListener {
            findNavController().navigate(R.id.action_landing_to_sign_up)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}