package com.example.shoppingscanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.shoppingscanner.cart.ProductAdapter
import com.example.shoppingscanner.databinding.FragmentPaymentCompletedBinding
import com.example.shoppingscanner.scanner.BarcodeScannerFragment

class PaymentCompletedFragment : Fragment() {
    private var visible: Boolean = false
    private var homeButton: Button? = null

    private lateinit var adapter: ProductAdapter

    private var _binding: FragmentPaymentCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPaymentCompletedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visible = true

        with(binding) {
            cartRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            cartRecyclerView.adapter = adapter
            homeButton = btnHome
        }

        navigateToHome()
    }

    private fun navigateToHome() {
        homeButton?.setOnClickListener(View.OnClickListener {
            val fragment = BarcodeScannerFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.payment_completed_fragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}