package com.example.onebeer.Market

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.BeerCarousel.CarouselAdapter
import com.example.onebeer.databinding.MarketPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class MarketPage: Fragment() {
    private var _binding: MarketPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = MarketPageBinding.inflate(inflater, container, false)

        val carouselIpaManager = LinearLayoutManager(context)
        carouselIpaManager.orientation = LinearLayoutManager.HORIZONTAL

        val carouselApaManager = LinearLayoutManager(context)
        carouselApaManager.orientation = LinearLayoutManager.HORIZONTAL

        val carouselUnknowManager = LinearLayoutManager(context)
        carouselUnknowManager.orientation = LinearLayoutManager.HORIZONTAL

        val carouselPilsenManager = LinearLayoutManager(context)
        carouselPilsenManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.beerCarouselIpa.layoutManager = carouselIpaManager
        binding.beerCarouselApa.layoutManager = carouselApaManager
        binding.beerCarouselPilsen.layoutManager = carouselPilsenManager
        binding.beerCarouselUnknow.layoutManager = carouselUnknowManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        db.collection("beers")
            .whereEqualTo("style", "IPA")
            .get()
            .addOnSuccessListener { beers ->
                val data: ArrayList<Beer> = ArrayList()
                beers.forEach { beer ->
                    data.add(Beer(
                        id = beer.id,
                        title = beer["title"] as String,
                        price = beer["price"] as Double,
                        ml = beer["ml"] as String,
                        style = beer["style"] as String,
                        description = beer["description"] as String,
                        imageUrl = beer["imageUrl"] as String,
                        quantity = null
                    ))
                }
                binding.beerCarouselIpa.adapter = context?.let { CarouselAdapter(data, it) }
            }
            .addOnFailureListener { exception ->
                Log.w("[ERROR]", "Error getting beers: ", exception)
            }

        db.collection("beers")
            .whereEqualTo("style", "APA")
            .get()
            .addOnSuccessListener {beers ->
                val data: ArrayList<Beer> = ArrayList()
                beers.forEach { beer ->
                    data.add(Beer(
                        id = beer.id,
                        title = beer["title"] as String,
                        price = beer["price"] as Double,
                        ml = beer["ml"] as String,
                        style = beer["style"] as String,
                        description = beer["description"] as String,
                        imageUrl = beer["imageUrl"] as String,
                        quantity = null
                    ))
                }
                binding.beerCarouselApa.adapter = context?.let { CarouselAdapter(data, it) }
            }
            .addOnFailureListener {exception ->
                Log.w("[ERROR]", "Error getting beers: ", exception)
            }

        db.collection("beers")
            .whereEqualTo("style", "PILSEN")
            .get()
            .addOnSuccessListener {beers ->
                val data: ArrayList<Beer> = ArrayList()
                beers.forEach { beer ->
                    data.add(Beer(
                        id = beer.id,
                        title = beer["title"] as String,
                        price = beer["price"] as Double,
                        ml = beer["ml"] as String,
                        style = beer["style"] as String,
                        description = beer["description"] as String,
                        imageUrl = beer["imageUrl"] as String,
                        quantity = null
                    ))
                }
                binding.beerCarouselPilsen.adapter = context?.let { CarouselAdapter(data, it) }
            }
            .addOnFailureListener {exception ->
                Log.w("[ERROR]", "Error getting beers: ", exception)
            }

        db.collection("beers")
            .whereEqualTo("style", "unknow")
            .get()
            .addOnSuccessListener { beers ->
                val data: ArrayList<Beer> = ArrayList()
                beers.forEach { beer ->
                    data.add(Beer(
                        id = beer.id,
                        title = beer["title"] as String,
                        price = beer["price"] as Double,
                        ml = beer["ml"] as String,
                        style = beer["style"] as String,
                        description = beer["description"] as String,
                        imageUrl = beer["imageUrl"] as String,
                        quantity = null
                    ))
                }
                binding.beerCarouselUnknow.adapter = context?.let { CarouselAdapter(data, it) }
            }
            .addOnFailureListener {exception ->
                Log.w("[ERROR]", "Error getting beers: ", exception)
            }
    }
}