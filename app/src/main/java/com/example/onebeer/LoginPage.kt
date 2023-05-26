package com.example.onebeer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onebeer.databinding.LoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginPage : Fragment() {

    private var _binding: LoginPageBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = LoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupTextNavigate.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_sign_up)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            var errorsCount = 0

            if (email == ""){
                errorsCount++
                binding.loginEmail.error = "Preencha o seu email."
            }
            if (password == ""){
                errorsCount++
                binding.loginPassword.error = "Preencha sua senha."
            }

            if (errorsCount == 0){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
//                            startActivity(Intent(context, HomeActivity::class.java))
                            Log.d("logInWithEmail:", "success")
                            findNavController().navigate(R.id.action_login_to_sign_up)
                        } else {
                            Log.d("signInWithEmail:", "not success")
                            Toast.makeText(
                                context,
                                "Autentificação falhou",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}