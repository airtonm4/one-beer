package com.example.onebeer.Profile.Historic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onebeer.Profile.Historic.HistoricList.Purchase
import com.example.onebeer.R
import com.example.onebeer.databinding.HistoricPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoricPage: Fragment() {
    private var _binding: HistoricPageBinding? = null
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        auth = Firebase.auth
        _binding = HistoricPageBinding.inflate(inflater, container, false)

        val recyclerManager = LinearLayoutManager(context)
        recyclerManager.orientation = LinearLayoutManager.VERTICAL

        binding.historicRecycle.layoutManager = recyclerManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        val purchasesArray: ArrayList<Purchase> = ArrayList()

        binding.aboutToolbar.setNavigationIcon(R.drawable.arrow_back_24)
        binding.aboutToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        /**
         * Parte que não funcionou.
         */
        db.collection("historic")
            .whereEqualTo("userId", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { purchases ->
                val auxMap: HashMap<String, Map<String, MutableList<Purchase>>> = hashMapOf()
                val auxArray: MutableMap<String, MutableList<Purchase>> = mutableMapOf()
                purchases.forEach { purchase ->
                    addValueToMultiMap(auxArray, "${purchase["shopId"]}", Purchase(
                        beerId = purchase["beerId"] as String,
                        shopId = purchase["shopId"] as String,
                        userId = purchase["userId"] as String,
                        quantity = purchase["quantity"] as Long,
                        total = purchase["total"] as Double
                    ))

                    auxMap["${purchase["shopId"]}"] = auxArray.filterKeys { key -> key == "${purchase["shopId"]}"}
                    Log.d("TAG",auxMap.toString())
                    auxMap.forEach {item ->
                        item.value.values.forEach { a ->
                            a.forEach {x ->
                                purchasesArray.add(
                                    Purchase(
                                        beerId = x.beerId,
                                        shopId = x.shopId,
                                        quantity = a.size.toLong(),
                                        userId = x.userId,
                                        total = x.total
                                    )
                                )
                            }
                        }
                    }
                }

            }
    }

    fun addValueToMultiMap(map: MutableMap<String, MutableList<Purchase>>, key: String, value: Purchase) {
        val existingValues = map[key]
        if (existingValues != null) {
            existingValues.add(value)
        } else {
            map[key] = mutableListOf(value)
        }
    }
}