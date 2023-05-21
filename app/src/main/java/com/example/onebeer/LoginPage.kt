package com.example.onebeer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onebeer.databinding.LoginPageBinding

class LoginPage : Fragment() {

    private var _binding: LoginPageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_sign_up)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}