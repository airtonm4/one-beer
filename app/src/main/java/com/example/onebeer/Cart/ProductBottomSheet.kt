package com.example.onebeer.Cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.onebeer.BeerCarousel.Beer
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
            context?.let {
                Glide.with(it)
                    .load(uri)
                    .placeholder(circularProgressDrawable)
                    .into(binding.productImage)
            }
        }

        binding.productTitle.text = beer.title
        binding.productMl.text = beer.ml
        binding.productDescription.text = beer.description
        binding.productPrice.text = "R$ ${beer.price}"
        binding.addProduct.text = "Adicionar R$ ${beer.price}"
        binding.countText.text = counter.toString()

        binding.incrementButton.setOnClickListener {
            this.counter++
            binding.countText.text = counter.toString()
            binding.addProduct.text = "Adicionar  R$ ${"%.2f".format(beer.price*counter)}"
        }

        binding.decrementButton.setOnClickListener {
            if (counter > 1){
                this.counter--
                binding.countText.text = counter.toString()
                binding.addProduct.text = "Adicionar  R$ ${"%.2f".format(beer.price*counter)}"
            }
        }

        binding.addProduct.setOnClickListener {
            db.collection("shop")
                .whereEqualTo("userId", currentUser!!.uid)
                .whereEqualTo("beerId", this.beer.id)
                .get()
                .addOnSuccessListener { shops ->
                    /**
                     * Checa se já existe esse item no carrinho, se não tiver ele vai adicionar ele como um novo item.
                     */
                    if (shops.isEmpty){
                        db.collection("shop")
                            .add(
                                hashMapOf(
                                    "userId" to currentUser.uid,
                                    "beerId" to this.beer.id,
                                    "quantity" to this.counter
                                )
                            ).addOnSuccessListener {
                                Toast.makeText(context, "Cerveja adicionada ao seu carrinho.", Toast.LENGTH_LONG).show()
                                dismiss()
                            }
                    } else {
                        /**
                         * Se caso esse usuário já tiver esse item no carrinho ele atualiza a quantidade dos itens no carrinho.
                         */
                        shops.forEach { shop ->
                            db.collection("shop")
                                .document(shop.id)
                                .update(
                                    hashMapOf(
                                        "beerId" to shop["beerId"],
                                        "userId" to currentUser.uid,
                                        "quantity" to this.counter
                                    ) as Map<String, Any>
                                ).addOnCompleteListener {
                                    Toast.makeText(context, "Cerveja foi atualizada no seu carrinho", Toast.LENGTH_LONG).show()
                                    dismiss()
                                }
                        }
                    }
                }

        }

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}