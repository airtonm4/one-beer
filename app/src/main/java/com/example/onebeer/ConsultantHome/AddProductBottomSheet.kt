package com.example.onebeer.ConsultantHome

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.onebeer.R
import com.example.onebeer.databinding.AddProductModalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddProductBottomSheet : BottomSheetDialogFragment() {
    private var _binding: AddProductModalBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddProductModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.beerImageAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(intent, 1000)
        }

        binding.buttonAddBeer.setOnClickListener {
            val nameBeer = binding.beerName
            val priceBeer = binding.beerPrice
            val descriptionBeer = binding.beerDescription
            val ml = binding.beerMl
            val style = binding.beerStyle
            val image = binding.beerImage

            var errorsCount: Int = 0

            if (nameBeer.text.toString() == "") {
                nameBeer.error = "Insira um nome."
                errorsCount++
            }

            if (priceBeer.text.toString() == "") {
                priceBeer.error = "Insira um preço."
                errorsCount++
            }

            if (descriptionBeer.text.toString() == "") {
                descriptionBeer.error = "Insira uma descrição."
                errorsCount++
            }

            if (ml.text.toString() == "") {
                ml.error = "Insira o Ml da cerveja."
                errorsCount++
            }

            if (style.text.toString() == "") {
                style.error = "Insira um estilo para a cerveja."
                errorsCount++
            }

            if (image.drawable == null) {
                errorsCount++
                Toast.makeText(context, "Insira uma imagem", Toast.LENGTH_LONG).show()
            }

            if (errorsCount == 0) {
                val storage = Firebase.storage
                val db = Firebase.firestore

                val pathString = "beers/${nameBeer.text.toString() + style.text.toString() + ml.text.toString()}"
                val imageReference = storage.reference.child(pathString)

                imageReference.putFile(this.imageUri).addOnCompleteListener {
                    Log.d("PATH", imageReference.path)
                    val circularProgressDrawable = this.context?.let { CircularProgressDrawable(it) }
                    if (circularProgressDrawable != null) {
                        circularProgressDrawable.strokeWidth = 8f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()
                        image.setImageDrawable(circularProgressDrawable)
                    }

                    storage.getReferenceFromUrl("gs://onebeer-d6603.appspot.com/${pathString}").downloadUrl.addOnSuccessListener { uri ->
                        context?.let {
                            Glide.with(it)
                                .load(uri)
                                .placeholder(circularProgressDrawable)
                                .into(image)
                        }
                    }

                    db.collection("beers")
                        .add(
                            hashMapOf(
                                "title" to nameBeer.text.toString(),
                                "description" to descriptionBeer.text.toString(),
                                "ml" to ml.text.toString(),
                                "style" to style.text.toString(),
                                "imageUrl" to "gs://onebeer-d6603.appspot.com/${pathString}",
                                "price" to priceBeer.text.toString().toDouble()
                            )
                        )
                        .addOnSuccessListener {
                            Toast.makeText(context, "Cerveja adicionada ao sistema.", Toast.LENGTH_LONG).show()
                            dismiss()
                        }

                }

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000){
            val returnUri = data!!.data

            if (returnUri != null) {
                this.imageUri = returnUri
            }

            val bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)
            binding.beerImage.setImageBitmap(bitmapImage)
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}