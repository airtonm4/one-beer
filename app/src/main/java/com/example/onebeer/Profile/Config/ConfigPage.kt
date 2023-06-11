package com.example.onebeer.Profile.Config

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onebeer.R
import com.example.onebeer.databinding.ConfigPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConfigPage: Fragment() {
    private var _binding: ConfigPageBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        auth = Firebase.auth
        _binding = ConfigPageBinding.inflate(inflater, container, false)

        binding.usernameText.text = auth.currentUser!!.displayName

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutToolbar.setNavigationIcon(R.drawable.arrow_back_24)
        binding.aboutToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.alterNameButton.setOnClickListener {
            val alterNameTextView = binding.alterEditText

            if (alterNameTextView.text.toString() == ""){
                alterNameTextView.error = "Insira um nome para alterar o anterior."
            } else {
                val db = Firebase.firestore
                val user = Firebase.auth.currentUser

                user!!.updateProfile(userProfileChangeRequest {
                    displayName = alterNameTextView.text.toString()
                })

                db.collection("users")
                    .whereEqualTo("userId", user.uid)
                    .get()
                    .addOnSuccessListener { userRetrieved ->
                        userRetrieved.forEach {
                            db.collection("users")
                                .document(it.id)
                                .update(
                                    hashMapOf(
                                        "userId" to user.uid,
                                        "type" to "customer",
                                        "name" to alterNameTextView.text.toString()
                                    ) as Map<String, Any>
                                )
                        }
                        binding.usernameText.text = alterNameTextView.text.toString()
                        binding.alterEditText.setText("")
                    }
            }
        }
    }
}