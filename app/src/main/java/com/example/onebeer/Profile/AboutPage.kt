package com.example.onebeer.Profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onebeer.R
import com.example.onebeer.databinding.AboutPageBinding

class AboutPage: Fragment() {

    private var _binding: AboutPageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = AboutPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutToolbar.setNavigationIcon(R.drawable.arrow_back_24)
        binding.aboutToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }
}