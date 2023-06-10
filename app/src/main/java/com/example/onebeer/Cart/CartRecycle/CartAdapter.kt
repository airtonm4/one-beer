package com.example.onebeer.Cart.CartRecycle

import android.annotation.SuppressLint
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
import com.example.onebeer.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class CartAdapter(private val dataSet: ArrayList<Beer>, private val context: Context, private val incrementListener: ItemClickListener): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carrinho_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]

        holder.name.text = dataSet.title
        holder.price.text = "R$ " + "%.2f".format(dataSet.price)
        holder.ml.text = dataSet.ml
        if (dataSet.quantity != null){
            holder.beerQuantity.text = dataSet.quantity.toString()
        }

        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        /**
         * Drawable de loading circular
         */
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 8f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val storage = Firebase.storage.getReferenceFromUrl(dataSet.imageUrl)
        val imageView = holder.image
        /**
         * Setando o loading da imagem.
         */
        imageView.setImageDrawable(circularProgressDrawable)
        /**
         * Fazendo o download e atualizando a ImageView com o resultado
         * por meio do Glide.
         */
        storage.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .placeholder(circularProgressDrawable)
                .into(imageView)
        }
        /**
         * ClickListener para excluir o produto do carrinho e para atualizar o preço
         * na página de carrinho.
         */
        holder.deleteButton.setOnClickListener {
            db.collection("shop")
                .whereEqualTo("userId", currentUser!!.uid)
                .whereEqualTo("beerId", dataSet.id)
                .get()
                .addOnSuccessListener { shops ->
                    shops.forEach { shop ->
                        shop.reference.delete()
                        this.dataSet.removeAt(position)
                        incrementListener.onItemClicked(holder, this.dataSet, position)
                        notifyItemRemoved(position)
                    }
                }
        }

        /**
         * ClickListener para atualizar a quantidade de produtos e para atualizar o preço
         * na página de carrinho.
         */
        holder.incrementButton.setOnClickListener {
            dataSet.quantity = dataSet.quantity!! + 1
            incrementListener.onItemClicked(holder, this.dataSet, position)
            notifyItemChanged(position)
        }

        /**
         * ClickListener para atualizar a quantidade de produtos e para atualizar o preço
         * na página de carrinho.
         */
        holder.decrementButton.setOnClickListener {
            if (dataSet.quantity!! > 1){
                dataSet.quantity = dataSet.quantity!! - 1
                incrementListener.onItemClicked(holder, this.dataSet, position)
                notifyItemChanged(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDeleted() {
        notifyDeleted()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.beer_image_cart)
        val name: TextView = itemView.findViewById<TextView>(R.id.product_cart_name)
        val price: TextView = itemView.findViewById<TextView>(R.id.product_cart_price)
        val ml: TextView = itemView.findViewById<TextView>(R.id.product_ml_cart)
        val beerQuantity: TextView = itemView.findViewById<TextView>(R.id.count_text)
        val deleteButton: ImageView = itemView.findViewById<ImageView>(R.id.delete_button)
        val incrementButton: ImageView = itemView.findViewById<ImageView>(R.id.increment_button)
        val decrementButton: ImageView = itemView.findViewById<ImageView>(R.id.decrement_button)
    }
}