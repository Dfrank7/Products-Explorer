package com.example.productexplorer.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productexplorer.databinding.ProductListFragmentBinding
import com.example.productexplorer.service.NetworkResult
import com.example.productexplorer.view.adapter.ProductListAdapter
import com.example.productexplorer.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment: Fragment() {
    private lateinit var binding: ProductListFragmentBinding
    private lateinit var adapter: ProductListAdapter
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProductListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProducts()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        adapter = ProductListAdapter(ProductListAdapter.ProductListener{

        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }
    }


    private fun observeProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsFlow.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is NetworkResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.submitList(result.data)

                            if (result.isFromCache) {
                                Toast.makeText(
                                    context,
                                    "Showing cached products",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is NetworkResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                result.message ?: "Unknown error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

}