package com.example.shoppingscanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shoppingscanner.R
import com.example.shoppingscanner.databinding.FragmentContinueWithBarcodeBinding


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

        cardView = binding.cardView
        continueWithBarcodeButton = binding.btncontinuewithbarcode
        onclick(continueWithBarcodeButton!!)

        /*
         cameraRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                navigateToBarcodeScannerFragment()
            } else {

            }
        }

         */

    }

    fun onclick(button: Button){
        button.setOnClickListener(){
            navigateToBarcodeScannerFragment(it)
            //cameraRequest?.launch(android.Manifest.permission.CAMERA)
        }
    }
    fun navigateToBarcodeScannerFragment(view:View) {
        val action = ContinueWithBarcodeFragmentDirections.actionContinueWithBarcodeFragmentToBarcodeScannerFragment()
        Navigation.findNavController(view).navigate(action)
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