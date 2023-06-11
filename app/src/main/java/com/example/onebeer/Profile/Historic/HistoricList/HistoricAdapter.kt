package com.example.onebeer.Profile.Historic.HistoricList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onebeer.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HistoricAdapter(private val dataSet: ArrayList<Purchase>, private val context: Context): RecyclerView.Adapter<HistoricAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historic_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]

        holder.price.text = "R$ " + "%.2f".format(dataSet.total)
        holder.quantity.text = "Compra de ${dataSet.quantity} itens."

        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser


    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.beer_image_historic)
        val price: TextView = itemView.findViewById(R.id.price_text)
        val quantity: TextView = itemView.findViewById(R.id.quantity_text)
    }
}