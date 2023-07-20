package com.example.shoppingscanner.scanner

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
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingscanner.cart.CartFragment
import com.example.shoppingscanner.R
import com.example.shoppingscanner.databinding.FragmentBarcodeScannerBinding
import com.example.shoppingscanner.model.Contributor
import com.example.shoppingscanner.model.Product
import com.example.shoppingscanner.model.Review
import com.example.shoppingscanner.model.Shipping
import com.example.shoppingscanner.model.Store
import com.example.shoppingscanner.model.Tax
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage


class BarcodeScannerFragment : Fragment() {
    @Suppress("InlinedApi")
    private var visible: Boolean = false
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    // views
    private var _binding: FragmentBarcodeScannerBinding? = null
    private var addToCartButton: Button? = null
    private var buyNowButton: Button? = null
    private var cameraPreview: ImageView? = null
    private var nameTextView: TextView? = null
    private var priceTextView: TextView? = null
    private var totalPriceTextView: TextView? = null

    // texts
    private var productText:String? = null
    private var priceText:String? = null
    private var totalPriceText:String? = null

    // barcode scanner
    private var barcodeScannerOptions: BarcodeScannerOptions? = null
    private var barcodeScanner: BarcodeScanner? = null

    // image
    private var imageBitmap:Bitmap? = null
    private val REQUEST_IMAGE_CAPTURE=1

    private var product: Product? = null

    //private val viewModel: ProductViewModel by viewModels()
    private lateinit var viewModel: ProductViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false)

        cameraPreview = binding.cameraPreview

        nameTextView = binding.productName
        priceTextView = binding.productPrice
        totalPriceTextView = binding.totalPrice

        productText = R.string.product.toString()
        priceText = R.string.price.toString()
        totalPriceText = R.string.price_sum.toString()


        buyNowButton = binding.btnbuynow
        addToCartButton = binding.btnaddtocart

        barcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS)
            .build()
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions!!)

        viewModel = ViewModelProvider(requireActivity())[ProductViewModel::class.java]

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        visible = true

        takeImage()

        buyNowButton?.setOnClickListener(){
            Log.d("buyNowButton", "buyNowButton: $product")
            product?.let { it1 -> addtoCart(it1) } // add to cart
            navigate() // navigate to cart screen
        }

        addToCartButton?.setOnClickListener(){
            this.product?.let { it1 -> addtoCart(it1) } // add to cart
        }

    }

    private fun addtoCart(product: Product) {
        Log.d("addtoCart", "addtoCart: $product")

        var productPrice = product.stores?.get(0)?.price?.toDouble()
        var cart = viewModel.cartProducts

        if (cart.get(product) != null) {
            cart.set(product, cart.get(product)!! + 1)
            if (productPrice != null) {
                productPrice *= cart.get(product)!!
                Log.d("addtoCart", "productPrice: $productPrice")
            }
        }else{
            cart.put(product, 1)
            Log.d("addtoCart", "1 productQuantity: $cart.get(product)")
        }
        totalPriceTextView!!.text = totalPriceText + " " + productPrice + " ₺"
        Log.d("addtoCart", "products: ${viewModel.cartProducts}")
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
                        showToast("Please try again.")
                        takeImage()
                    }

                    for (barcode in barcodes) {
                        Log.d("barcode", barcode.rawValue.toString())
                        //getProductDetails("3614272049529")
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
    private fun getProductDetails2(barcode: String) {

        Log.d("getProductDetails", "getProductDetails: $barcode")
        val product1 = Product(
            barcode_number = "1234567890123",
            barcode_formats = "UPC",
            mpn = "MPN123",
            model = "Model 1",
            asin = "B01ABCXYZ",
            title = "Ürün 1",
            category = "Elektronik",
            manufacturer = "Üretici 1",
            brand = "Marka 1",
            contributors = listOf(
                Contributor(role = "Tasarımcı", name = "Tasarımcı 1"),
                Contributor(role = "Mühendis", name = "Mühendis 1")
            ),
            age_group = "Yetişkin",
            ingredients = "Malzeme 1, Malzeme 2",
            nutrition_facts = "Kalori: 100, Protein: 5g, Yağ: 2g",
            energy_efficiency_class = "A+",
            color = "Siyah",
            gender = "Unisex",
            material = "Plastik",
            pattern = "Düz",
            format = "CD",
            multipack = "Evet",
            size = "L",
            length = "20 cm",
            width = "15 cm",
            height = "5 cm",
            weight = "500 gr",
            release_date = "01/01/2023",
            description = "Bu ürünün açıklaması.",
            features = listOf("Özellik 1", "Özellik 2"),
            images = listOf("image1.jpg", "image2.jpg"),
            last_update = "19/07/2023",
            stores = listOf(
                Store(
                    name = "Mağaza 1",
                    country = "Türkiye",
                    currency = "TRY",
                    currency_symbol = "₺",
                    price = "100.00",
                    sale_price = "80.00",
                    tax = listOf(Tax(country = "Türkiye", region = "İstanbul", rate = "18%", tax_ship = "True")),
                    link = "https://magaza1.com/urun1",
                    item_group_id = "GRP123",
                    availability = "Stokta",
                    condition = "Yeni",
                    shipping = listOf(Shipping(country = "Türkiye", region = "İstanbul", service = "Standart", price = "5.00")),
                    last_update = "19/07/2023"
                )
            ),
            reviews = listOf(
                Review(
                    name = "Kullanıcı 1",
                    rating = "5",
                    title = "Harika Ürün!",
                    review = "Bu ürünü çok sevdim. Kaliteli ve işlevsel.",
                    date = "10/07/2023"
                ),
                Review(
                    name = "Kullanıcı 2",
                    rating = "4",
                    title = "Fiyat Performans İyi",
                    review = "Bu fiyata bu ürünü almak kesinlikle değerli.",
                    date = "12/07/2023"
                )
            )
        )
        this.product=product1
        val productName = product1.title
        val productPrice = product1.stores?.get(0)?.price.toString()

        nameTextView!!.text = productText + " " + productName
        priceTextView!!.text = priceText + " " + productPrice + " ₺"
        totalPriceTextView!!.text = totalPriceText + " " + productPrice + " ₺"
    }

    private fun getProductDetails(barcode: String) {
        viewModel.getDataFromAPI(barcode)
        Log.d("get data ", barcode)
        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                Log.d("observe product", product.toString())
                this.product=product
                val productName = it.title
                val productPrice = it.stores?.get(0)?.price.toString()

                nameTextView!!.text = productText + " " + productName
                priceTextView!!.text = priceText + " " + productPrice + " ₺"
                totalPriceTextView!!.text = totalPriceText + " " + productPrice + " ₺"
            } ?: run {
                showToast("No product found or an error occurred.")
                Log.e("Product Details Error", "No product found or an error occurred.")
            }
        }
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

    fun navigate(){
        Log.d("navigate","navigating")
            val fragment = CartFragment()
            setVisibility()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.barcodeScannerFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
    }
    fun setVisibility() {
        buyNowButton!!.visibility = View.GONE
        addToCartButton!!.visibility = View.GONE
        cameraPreview!!.visibility = View.GONE
        nameTextView!!.visibility = View.GONE
        priceTextView!!.visibility = View.GONE
        totalPriceTextView!!.visibility = View.GONE
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