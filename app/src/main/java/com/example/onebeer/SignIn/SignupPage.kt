package com.example.onebeer.SignIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onebeer.ConsultantHome.ConsultantHomeActivity
import com.example.onebeer.Home.HomeActivity
import com.example.onebeer.R
import com.example.onebeer.databinding.SingupPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
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
            /**
             * Validando se todos os campos foram preenchidos.
             */
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
            /**
             * Caso um não tenha sido preenchido não realiza as request de auth.
             */
            if (errorsCount == 0){
                /**
                 * Criando um usuário com os dados passados.
                 */
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { singup ->
                        if (singup.isSuccessful){
                            val db = Firebase.firestore
                            /**
                             * Com o Sign Up sendo sucedido, podemos tentar realizar o login de forma imediata.
                             */
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener{ login ->
                                    if (login.isSuccessful) {
                                        /**
                                         * Atualizando o profile para que ele possa receber o nome do usuário.
                                         */
                                        val user = Firebase.auth.currentUser
                                        user!!.updateProfile(userProfileChangeRequest {
                                            displayName = name
                                        })
                                        /**
                                         * Adicionar os atributos para o usuário.
                                         */
                                        db.collection("users")
                                            .add(
                                                hashMapOf(
                                                    "userId" to user.uid,
                                                    "type" to "customer",
                                                    "name" to name
                                                )
                                            )
                                        /**
                                         * Checa se o usuário é admin ou não.
                                         */
                                        db.collection("users")
                                            .whereEqualTo("userId", user.uid)
                                            .get()
                                            .addOnSuccessListener {userRetrieved ->
                                                userRetrieved.forEach { checkUser ->
                                                    if (checkUser["type"] == "admin"){
                                                        startActivity(Intent(context, ConsultantHomeActivity::class.java))
                                                    } else {
                                                        startActivity(Intent(context, HomeActivity::class.java))
                                                    }
                                                }
                                            }
                                    } else {
                                        Log.d("Sing Up", "User created, but login fails.")
                                        findNavController().navigate(R.id.action_sign_up_to_login)
                                    }
                                }
                        } else {
                            Log.d("Sign Up:", "not success")
                            Toast.makeText(
                                context,
                                "Sign Up falhou, tente novamente.",
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}