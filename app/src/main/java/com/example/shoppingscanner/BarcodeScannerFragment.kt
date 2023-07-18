package com.example.shoppingscanner

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingscanner.databinding.FragmentBarcodeScannerBinding
import com.example.shoppingscanner.model.BarcodeResponse
import com.example.shoppingscanner.network.BarcodeService
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit



class BarcodeScannerFragment : Fragment() {
    @Suppress("InlinedApi")
    private var visible: Boolean = false
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentBarcodeScannerBinding? = null
    private var addToCartButton: Button? = null
    private var buyNowButton: Button? = null
    private var cameraPreview: ImageView? = null
    private var productText:TextView? = null
    private var priceText:TextView? = null
    private var totalPriceText:TextView? = null

    private var barcodeScannerOptions: BarcodeScannerOptions? = null
    private var barcodeScanner: BarcodeScanner? = null

    private var imageBitmap:Bitmap? = null //
    private val REQUEST_IMAGE_CAPTURE=1 //


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false)

        cameraPreview = binding.cameraPreview

        productText = binding.productName
        priceText = binding.productPrice
        totalPriceText = binding.totalPrice


        buyNowButton = binding.btnbuynow
        addToCartButton = binding.btnaddtocart

        barcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS)
            .build()

        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions!!)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visible = true

        takeImage()
        
        navigate(buyNowButton!!)
        navigate(addToCartButton!!)

    }
    private fun takeImage()
    {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE)
        } catch (e:Exception){
            Log.e("Error taking image",e.message.toString())
            showToast("Please try again.")
        }
    }
    private fun processImage() {
        if (imageBitmap!=null){
            val inputImage = InputImage.fromBitmap(imageBitmap!!,0)
            val scanner = BarcodeScanning.getClient(barcodeScannerOptions!!)
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->

                    if (barcodes.toString() == "[]") {
                        showToast("No barcode found")
                    }

                    for (barcode in barcodes) {
                        Log.d("raw", barcode.rawValue.toString())
                        
                        getProductDetails(barcode.rawValue.toString())
                    }
                }
                .addOnFailureListener {

                }
        }
        else{
            showToast("Please take an image")
        }
    }

    private fun getProductDetails(barcode: String) {
        Log.d("product details", barcode)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.barkodoku.com/ws/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()


        val service: BarcodeService = retrofit.create(BarcodeService::class.java)

        val apiKey = "17eb1a1af4ef0440c6f571d1dba4718e1068fb498c00bd91f9d7a472e2e0ff6c"

        val call = service.getProductDetails(apiKey, barcode)

        call.enqueue(object : Callback<BarcodeResponse> {

            override fun onResponse(
                call: Call<BarcodeResponse>,
                response: Response<BarcodeResponse>
            ) {
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    Log.d("response successfull", response.body().toString())
                    val barcodeResponse = response.body()

                    val productBarcode = barcodeResponse?.BarkodGetirResult?.UrunBarkod
                    if (productBarcode != null) {
                        Log.d("barcode", productBarcode.toString())
                        val productName = productBarcode.UrunAd
                        val productPrice = productBarcode.UrunDetay

                        Log.d("name", productName.toString())
                        Log.d("price", productPrice.toString())

                        productText!!.text = productText!!.text.toString() + " " + productName
                        priceText!!.text = priceText!!.text.toString() + " " + productPrice

                        productText!!.invalidate()
                        priceText!!.invalidate()
                    } else {
                        showToast("No product found")
                        Log.e("no product", response.message())
                    }
                } else {
                    Log.e("Error", response.message())
                }
            }

            override fun onFailure(call: Call<BarcodeResponse>, t: Throwable) {
                Log.e("Onfailure", t.message.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            val extras: Bundle? = data?.extras
            imageBitmap= extras?.get("data") as Bitmap

            if (imageBitmap!=null) {
                binding.cameraPreview.setImageBitmap(imageBitmap)
                Log.d("onactivityresult","Image not null")
                processImage()
            }
        }
    }

    fun navigate(button: Button){
        button.setOnClickListener(){
            val fragment = CartFragment()
            button!!.visibility = View.GONE
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.barcodeScannerFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        buyNowButton = null
        addToCartButton = null
        fullscreenContent = null
        fullscreenContentControls = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}