package com.example.onebeer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onebeer.databinding.SingupPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupPage: Fragment() {

    private var _binding: SingupPageBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = SingupPageBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignup.setOnClickListener{
            val name = binding.singUpName.text.toString()
            val email = binding.signUpEmail.text.toString()
            val password = binding.singUpPassword.text.toString()

            var errorsCount = 0

            if (name == "") {
                errorsCount++
                binding.singUpName.error = "Preencha o seu nome."
            }
            if (email == ""){
                errorsCount++
                binding.signUpEmail.error = "Preencha o seu email."
            }
            if (password == ""){
                errorsCount++
                binding.singUpPassword.error = "Preencha sua senha."
            }

            if (errorsCount == 0){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
//                            startActivity(Intent(context, HomeActivity::class.java))
                            Log.d("signInWithEmail:", "success")
                            findNavController().navigate(R.id.action_sign_up_to_login)
                        } else {
                            Log.d("signInWithEmail:", "not success")
                            Log.w("task", task.exception)
                            Toast.makeText(
                                context,
                                "Autentificação falhou",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }

        }
        binding.loginTextNavigate.setOnClickListener {
            findNavController().navigate(R.id.action_sign_up_to_login)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}