package com.example.onebeer.Cart.CartRecycle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.onebeer.BeerCarousel.Beer
import com.example.onebeer.BeerCarousel.CarouselAdapter
import com.example.onebeer.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class CartAdapter(private val dataSet: List<Beer>, private val context: Context): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carrinho_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]
        holder.name.text = dataSet.title
        holder.price.text = dataSet.price.toString()
        holder.ml.text = dataSet.ml
        if (dataSet.quantity != null){
            holder.beerQuantity.text = dataSet.quantity.toString()
        }

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 8f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val storage = Firebase.storage.getReferenceFromUrl(dataSet.imageUrl)
        val imageView = holder.image

        imageView.setImageDrawable(circularProgressDrawable)

        storage.downloadUrl.addOnSuccessListener { uri ->

            Glide.with(context)
                .load(uri)
                .placeholder(circularProgressDrawable)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.beer_carousel_image_carrinho)
        val name: TextView = itemView.findViewById<TextView>(R.id.nomeDoProduto_carrinho)
        val price: TextView = itemView.findViewById<TextView>(R.id.preco_carrinho)
        val ml: TextView = itemView.findViewById<TextView>(R.id.ml_carrinho)
        val beerQuantity: TextView = itemView.findViewById<TextView>(R.id.beer_quantity)
    }
}