package com.example.onebeer.Profile

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.onebeer.databinding.ProfilePageBinding


class ProfilePage: Fragment() {

    private var _binding: ProfilePageBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

}