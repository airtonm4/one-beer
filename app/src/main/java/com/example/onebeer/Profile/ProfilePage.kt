package com.example.onebeer.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.onebeer.MainActivity
import com.example.onebeer.Profile.About.AboutPage
import com.example.onebeer.Profile.Config.ConfigPage
import com.example.onebeer.Profile.Historic.HistoricPage
import com.example.onebeer.Profile.Security.SecurityPage
import com.example.onebeer.R
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * De acordo com o botão selecionado ele vai trocar o fragment ou então vai deslogar
         * o usuário e o enviar para 
         */
        binding.historicButton.setOnClickListener {
            val historicPage = HistoricPage()
            requireActivity().supportFragmentManager.commit {
                replace(R.id.container, historicPage, "HistoricPage")
                    .addToBackStack(null)
            }
        }

        binding.securityButton.setOnClickListener {
            val securityPage = SecurityPage()
            requireActivity().supportFragmentManager.commit {
                replace(R.id.container, securityPage, "HistoricPage")
                    .addToBackStack(null)
            }
        }

        binding.configButton.setOnClickListener {
            val configPage = ConfigPage()
            requireActivity().supportFragmentManager.commit {
                replace(R.id.container, configPage, "HistoricPage")
                    .addToBackStack(null)
            }
        }

        binding.aboutButton.setOnClickListener {
            val aboutPage = AboutPage()
            requireActivity().supportFragmentManager.commit {
                replace(R.id.container, aboutPage, "AboutPage")
                    .addToBackStack(null)
            }
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, MainActivity::class.java))
        }

    }

}