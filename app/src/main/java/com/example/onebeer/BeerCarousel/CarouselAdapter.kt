package com.example.onebeer.BeerCarousel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.RecyclerListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.onebeer.Cart.ProductBottomSheet
import com.example.onebeer.Home.HomeActivity
import com.example.onebeer.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CarouselAdapter(private val dataSet: ArrayList<Beer>, private val context: Context): Adapter<CarouselAdapter.ViewHolder>() {
    private lateinit var itemListener: RecyclerListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]
        holder.title.text = dataSet.title
        holder.price.text = "R$ " + dataSet.price.toString()
        holder.ml.text = dataSet.ml

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

        holder.itemView.setOnClickListener {
            val modalBottomSheet = ProductBottomSheet(dataSet)
            val ft = (context as HomeActivity).supportFragmentManager.beginTransaction()
            modalBottomSheet.show(ft, ProductBottomSheet.TAG)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.beer_carousel_image)
        val title: TextView = itemView.findViewById<TextView>(R.id.beer_carousel_title)
        val price: TextView = itemView.findViewById<TextView>(R.id.beer_carousel_price)
        val ml: TextView = itemView.findViewById<TextView>(R.id.beer_carousel_ml)
    }

}