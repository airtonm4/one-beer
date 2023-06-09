package com.example.onebeer.Cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.onebeer.R
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.CartRecycle.Shop
import com.example.onebeer.databinding.MarketPageBinding
import com.example.onebeer.databinding.ProductBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProductBottomSheet(private val beer: Beer) : BottomSheetDialogFragment() {
    private var _binding: ProductBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var counter: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = Firebase.storage.getReferenceFromUrl(beer.imageUrl)
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        val circularProgressDrawable = this.context?.let { CircularProgressDrawable(it) }
        if (circularProgressDrawable != null) {
            circularProgressDrawable.strokeWidth = 8f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            binding.productImage.setImageDrawable(circularProgressDrawable)
        }

        storage.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .placeholder(circularProgressDrawable)
                .into(binding.productImage)
        }

        binding.productTitle.text = beer.title
        binding.productMl.text = beer.ml
        binding.productDescription.text = beer.description
        binding.productPrice.text = "R$ ${beer.price}"
        Log.d("Button text", binding.addProduct.text as String)
        binding.addProduct.text = "Adicionar ${beer.price}"
        binding.countText.text = counter.toString()

        binding.incrementButton.setOnClickListener {
            this.counter++
            binding.countText.text = counter.toString()
        }

        binding.decrementButton.setOnClickListener {
            if (counter > 1){
                this.counter--
                binding.countText.text = counter.toString()
            }
        }

        binding.addProduct.setOnClickListener {
            db.collection("shop")
                .add(
                    hashMapOf(
                        "userId" to currentUser!!.uid,
                        "beers" to listOf(Shop(this.beer.id, this.counter))
                    )
                ).addOnSuccessListener {
                    Toast.makeText(context, "Cerveja adicionada ao seu carrinho.", Toast.LENGTH_LONG).show()
                    dismiss()
                }
        }

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}