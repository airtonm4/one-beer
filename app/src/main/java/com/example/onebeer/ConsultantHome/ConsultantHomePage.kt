package com.example.onebeer.ConsultantHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.Cart.CartRecycle.CartAdapter
import com.example.onebeer.Cart.CartRecycle.ItemClickListener
import com.example.onebeer.databinding.ConsultantHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConsultantHomePage: Fragment() {
    private var _binding: ConsultantHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = ConsultantHomeBinding.inflate(inflater, container, false)


        val carouselManager = LinearLayoutManager(context)
        carouselManager.orientation = LinearLayoutManager.VERTICAL

        binding.beersRecycle.layoutManager = carouselManager

        binding.beersRecycle.setScrollingTouchSlop(0)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incrementListener: ItemClickListener = object : ItemClickListener {
            override fun onItemClicked(vh: CartAdapter.ViewHolder?, dataSet: ArrayList<Beer>?, pos: Int) {

            }
        }

        val db = Firebase.firestore

        db.collection("beers")
            .get()
            .addOnSuccessListener { beers ->
                val data: ArrayList<Beer> = ArrayList()
                beers.forEach { beer ->
                    data.add(
                        Beer(
                            id = beer.id,
                            title = beer["title"] as String,
                            price = beer["price"] as Double,
                            ml = beer["ml"] as String,
                            style = beer["style"] as String,
                            description = beer["description"] as String,
                            imageUrl = beer["imageUrl"] as String,
                            quantity = null
                        )
                    )
                }
                binding.beersRecycle.adapter = context?.let { CartAdapter(data, it, incrementListener) }
            }
    }
}