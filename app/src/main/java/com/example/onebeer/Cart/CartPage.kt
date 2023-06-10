package com.example.onebeer.Cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.Cart.CartRecycle.CartAdapter
import com.example.onebeer.Cart.CartRecycle.ItemClickListener
import com.example.onebeer.databinding.CartPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CartPage : Fragment(){
    private var _binding: CartPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private var beers: ArrayList<Beer> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = CartPageBinding.inflate(inflater, container, false)

        val recyclerManager = LinearLayoutManager(context)
        recyclerManager.orientation = LinearLayoutManager.VERTICAL

        binding.cartRecycler.layoutManager = recyclerManager

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        var total: Double = 0.0;

        /**
         * ItemClickListener que é retornado do CartAdapter para a CartPage.
         * Com ele recalculamos o total da compra de acordo com as ações do RecycleView.
         */
        val incrementListener: ItemClickListener = object : ItemClickListener {
            override fun onItemClicked(vh: CartAdapter.ViewHolder?, dataSet: ArrayList<Beer>?, pos: Int) {
                total = 0.0
                dataSet?.forEach { beer ->
                    total += beer.price as Double * beer.quantity as Long
                    binding.totalPrice.text = "R$ " + "%.2f".format(total)
                }
            }
        }

        /**
         * Request para pegar todos os produtos da compra.
         */
        db.collection("shop")
            .whereEqualTo("userId", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { shop ->

                shop.forEach { shopItem ->
                    /**
                     * Request para pegar todas as cervejas presentes nessa compra.
                     */
                    Log.d("SHOP", "beerId ${shopItem["beerId"]}")
                    db.collection("beers")
                        .document(shopItem["beerId"] as String)
                        .get()
                        .addOnSuccessListener { beer ->
                            Log.d("BEER", "DATA -> ${beer.data}")
                            this.beers.add(Beer(
                                id = beer.id,
                                title = beer["title"] as String,
                                price = beer["price"] as Double,
                                ml = beer["ml"] as String,
                                style = beer["style"] as String,
                                description = beer["description"] as String,
                                imageUrl = beer["imageUrl"] as String,
                                quantity = shopItem["quantity"] as Long
                            ))
                            Log.d("PRICE", "${beer["price"]}")
                            Log.d("QUANTITY", "${shopItem["quantity"]}")
                            total += beer["price"] as Double * shopItem["quantity"] as Long
                            binding.totalPrice.text = "R$ " + "%.2f".format(total)
                            binding.cartRecycler.adapter = context?.let { CartAdapter(this.beers, it, incrementListener) }
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("[ERROR]", "Error getting beers: ", exception)
            }

        binding.cartRecycler.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            if (!binding.cartRecycler.canScrollVertically(1)){
                /**
                 * TODO: Adicionar o infinite scroll aqui.
                 */
            }
        }
    }

}