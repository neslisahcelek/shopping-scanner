package com.example.shoppingscanner.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingscanner.PaymentCompletedFragment
import com.example.shoppingscanner.R
import com.example.shoppingscanner.databinding.FragmentCartBinding
import com.example.shoppingscanner.model.Product
import com.example.shoppingscanner.scanner.ProductViewModel

class CartFragment : Fragment() {

    private var visible: Boolean = false
    private var cartText:TextView? = null
    private var payButton: Button? = null

    private lateinit var adapter: ProductAdapter

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ProductViewModel::class.java]
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ProductAdapter(viewModel.cartProducts as HashMap<Product, Int>)
        visible = true
        Log.d("cart", "onViewCreated: ${viewModel.cartProducts} ")


        with(binding) {
            cartRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            cartRecyclerView.adapter = adapter
            Log.d("cart", "onViewCreated: ${viewModel.cartProducts} $adapter")
            cartText  = myCart
            payButton = btnPayWithHadi
        }

        navigateToPay()
    }

    private fun navigateToPay() {
        payButton?.setOnClickListener(View.OnClickListener {
            binding.cartRecyclerView.visibility = View.GONE
            binding.myCart.visibility = View.GONE
            binding.btnPayWithHadi.visibility = View.GONE

            val fragment = PaymentCompletedFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.cart_fragment, fragment)
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
        cartText = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}