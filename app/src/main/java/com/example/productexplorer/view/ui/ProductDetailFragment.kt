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
import androidx.navigation.fragment.navArgs
import com.example.productexplorer.databinding.ProductDetailFragmentBinding
import com.example.productexplorer.service.NetworkResult
import com.example.productexplorer.utility.loadPicture
import com.example.productexplorer.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {
    private lateinit var binding: ProductDetailFragmentBinding

    private val viewModel: ProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProductDetailFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId
        viewModel.fetchProductById(productId)

        observeProductDetails()
    }

    private fun observeProductDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetailFlow.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.contentContainer.visibility = View.GONE
                        }
                        is NetworkResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.contentContainer.visibility = View.VISIBLE

                            result.data?.let { product ->
                                binding.apply {
                                    titleTv.text = product.title
                                    descriptionTv.text = product.description
                                    priceTv.text = "$${product.price}"
                                    categoryTv.text = product.category
                                    loadPicture(requireActivity(), product.image, productIv)
//                                    textViewRating.text = "Rating: ${product.rating.rate} (${product.rating.count} reviews)"
                                }

                                if (result.isFromCache) {
                                    Toast.makeText(
                                        context,
                                        "Showing cached product details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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