package com.example.onebeer.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.BeerCarousel.CarouselAdapter
import com.example.onebeer.CartRecycle.CartAdapter
import com.example.onebeer.databinding.CartPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CartPage : Fragment(){
    private var _binding: CartPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = CartPageBinding.inflate(inflater, container, false)

        val recyclerManager = LinearLayoutManager(context)
        recyclerManager.orientation = LinearLayoutManager.VERTICAL

        val mockBeers: List<Beer> = listOf(
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", ""),
            Beer("Baden", "R$ 20,00", 300, "aaaaa", "IPA", "")
        )
        binding.beerRecycler.adapter = CartAdapter(mockBeers)
        binding.beerRecycler.layoutManager = recyclerManager

        return binding.root
    }

}