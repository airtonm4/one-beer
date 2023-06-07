package com.example.onebeer.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.BeerCarousel.CarouselAdapter
import com.example.onebeer.databinding.HomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomePage: Fragment() {

    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = HomePageBinding.inflate(inflater, container, false)

        val carouselManager = LinearLayoutManager(context)
        carouselManager.orientation = LinearLayoutManager.HORIZONTAL

        val mockBeers: List<Beer> = listOf(
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", "")
        )
        binding.beerCarousel.adapter = CarouselAdapter(mockBeers)
        binding.beerCarousel.layoutManager = carouselManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = auth.currentUser!!.displayName
    }
}