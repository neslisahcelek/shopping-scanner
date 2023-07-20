package com.example.shoppingscanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.shoppingscanner.databinding.FragmentContinueWithBarcodeBinding
import com.example.shoppingscanner.scanner.BarcodeScannerFragment


class ContinueWithBarcodeFragment : Fragment() {

    private var visible: Boolean = false
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentContinueWithBarcodeBinding? = null
    private var continueWithBarcodeButton: Button? = null
    private var cameraRequest:ActivityResultLauncher<String>?= null
    private var cardView:CardView? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContinueWithBarcodeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visible = true

        /*
         cameraRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                navigateToBarcodeScannerFragment()
            } else {

            }
        }
         */


        cardView = binding.cardView
        continueWithBarcodeButton = binding.btncontinuewithbarcode
        onclick(continueWithBarcodeButton!!)

    }

    fun onclick(button: Button){
        button.setOnClickListener(){
            navigateToBarcodeScannerFragment()
            //cameraRequest?.launch(android.Manifest.permission.CAMERA)
        }
    }
    fun navigateToBarcodeScannerFragment() {
        val fragment = BarcodeScannerFragment()
        continueWithBarcodeButton!!.visibility = View.GONE
        cardView!!.visibility = View.GONE
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.continueWithBarcodeFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        continueWithBarcodeButton = null
        fullscreenContent = null
        fullscreenContentControls = null
        cardView = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}