package com.example.onebeer.Home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.BeerCarousel.CarouselAdapter
import com.example.onebeer.Cart.ProductBottomSheet
import com.example.onebeer.databinding.HomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class HomePage: Fragment() {

    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var olariaData: Beer

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = HomePageBinding.inflate(inflater, container, false)


        val carouselManager = LinearLayoutManager(context)
        carouselManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.beerCarousel.layoutManager = carouselManager

        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        val circularProgressDrawable = this.context?.let { CircularProgressDrawable(it) }
        if (circularProgressDrawable != null) {
            circularProgressDrawable.strokeWidth = 8f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            binding.cardImage.setImageDrawable(circularProgressDrawable)
        }
        /**
         * Busca informação de um produto especifico
         */
        db.collection("beers")
            .document("DNPRVCIn2r1utlG5ZbJe")
            .get()
            .addOnSuccessListener { beer ->
                val storage = Firebase.storage.getReferenceFromUrl(beer["imageUrl"] as String)

                storage.downloadUrl.addOnSuccessListener { uri ->
                    context?.let {
                        Glide.with(it)
                            .load(uri)
                            .placeholder(circularProgressDrawable)
                            .into(binding.cardImage)
                    }
                }

                binding.cardTitle.text = "Cerveja ${beer["title"]}"
                binding.cardDescription.text = beer["description"] as String
                binding.cardMl.text = beer["ml"] as String
                binding.cardPrice.text = "R$ ${beer["price"]}"

                this.olariaData = Beer(
                    id = beer.id,
                    title = beer["title"] as String,
                    price = beer["price"] as Double,
                    ml = beer["ml"] as String,
                    style = beer["style"] as String,
                    description = beer["description"] as String,
                    imageUrl = beer["imageUrl"] as String,
                    quantity = null
                )
            }
        /**
         * Busca seis produtos aleatorios para compor o carousel.
         */
        db.collection("beers")
            .limit(6)
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
                binding.beerCarousel.adapter = context?.let { CarouselAdapter(data, it) }
            }
        /**
         * Abre modal para adicionar novo produto ao carrinho quando clicado na imagem do produto destacado.
         */
        binding.cardImage.setOnClickListener {
            val modalBottomSheet = ProductBottomSheet(olariaData)
            val ft = (context as HomeActivity).supportFragmentManager.beginTransaction()
            modalBottomSheet.show(ft, ProductBottomSheet.TAG)
        }
    }
}