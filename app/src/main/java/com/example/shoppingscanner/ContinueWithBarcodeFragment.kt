package com.example.shoppingscanner

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shoppingscanner.databinding.FragmentContinueWithBarcodeBinding


class ContinueWithBarcodeFragment : Fragment() {

    @Suppress("InlinedApi")
    private var visible: Boolean = false
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentContinueWithBarcodeBinding? = null
    private var continueWithBarcodeButton: Button? = null

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

        continueWithBarcodeButton = binding.btncontinuewithbarcode
        navigate(continueWithBarcodeButton!!)

    }

    fun navigate(button: Button){
        button.setOnClickListener(){
            val fragment = BarcodeScannerFragment()
            continueWithBarcodeButton!!.visibility = View.GONE
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.continueWithBarcodeFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        continueWithBarcodeButton = null
        fullscreenContent = null
        fullscreenContentControls = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}