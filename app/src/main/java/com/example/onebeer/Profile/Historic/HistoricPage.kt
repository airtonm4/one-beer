package com.example.onebeer.Profile.Historic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onebeer.R
import com.example.onebeer.databinding.HistoricPageBinding

class HistoricPage: Fragment() {
    private var _binding: HistoricPageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = HistoricPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutToolbar.setNavigationIcon(R.drawable.arrow_back_24)
        binding.aboutToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }
}