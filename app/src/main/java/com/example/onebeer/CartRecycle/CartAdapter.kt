package com.example.onebeer.CartRecycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.BeerCarousel.CarouselAdapter
import com.example.onebeer.R

class CartAdapter(private val dataSet: List<Beer>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carrinho_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]
        holder.nome_cerveja.text = dataSet.title
        holder.price.text = dataSet.price.toString()
        //passar a imagem aqui
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.beer_carousel_image_carrinho)
        val nome_cerveja = itemView.findViewById<TextView>(R.id.nomeDoProduto_carrinho)
        val price = itemView.findViewById<TextView>(R.id.preco_carrinho)
        val ml_cart = itemView.findViewById<TextView>(R.id.ml_carrinho)
    }
}