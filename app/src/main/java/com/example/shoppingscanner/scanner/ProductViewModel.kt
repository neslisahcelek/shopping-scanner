package com.example.shoppingscanner.scanner

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingscanner.model.Product
import com.example.shoppingscanner.model.ProductResponse
import com.example.shoppingscanner.network.barcodeAPI
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val barcodeApi = barcodeAPI.retrofitService
    val cartProducts = HashMap< Product,Int>()

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

    private val _productResponse = MutableLiveData<ProductResponse>()
    val productResponse: LiveData<ProductResponse>
        get() = _productResponse



    fun getDataFromAPI(barcode:String) {
        viewModelScope.launch {
            try {
                Log.d(ContentValues.TAG, "launch: $barcode")
                val response = barcodeApi.getProductDetails(barcode=barcode)
                _productResponse.value=response
                _product.value = response.products?.get(0)

            } catch (e: Exception) {
                e.message?.let { Log.d("api error", it) }
            }
        }
    }
}