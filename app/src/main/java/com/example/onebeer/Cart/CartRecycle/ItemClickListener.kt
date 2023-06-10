package com.example.onebeer.Cart.CartRecycle

import com.example.onebeer.BeerCarousel.Beer

/**
 * Classe para enviar o evento de remover produto, aumentar a quantidade de produto e
 * de diminuir a quantidade de produto.
 */
interface ItemClickListener {
    fun onItemClicked(vh: CartAdapter.ViewHolder?, dataSet: ArrayList<Beer>?, pos: Int)
}