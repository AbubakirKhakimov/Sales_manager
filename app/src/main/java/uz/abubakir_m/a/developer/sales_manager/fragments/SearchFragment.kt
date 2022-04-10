package uz.abubakir_m.a.developer.sales_manager.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.adapters.SalesAdapter
import uz.abubakir_m.a.developer.sales_manager.adapters.SalesCallBack
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentSearchBinding
import uz.abubakir_m.a.developer.sales_manager.models.Sales
import uz.abubakir_m.a.developer.sales_manager.models.MainViewModel

class SearchFragment : Fragment(), SalesCallBack {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: MainViewModel

    lateinit var salesAdapter: SalesAdapter
    var searchResultsList = ArrayList<Sales>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        salesAdapter = SalesAdapter(searchResultsList, this)
        binding.searchResultRv.adapter = salesAdapter
        requestFocus()

        binding.searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                isLoading(true)
                viewModel.loadSalesSearchResults(binding.searchView.text.toString().trim())
            }
            false
        }

    }

    private fun requestFocus(){
        binding.searchView.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun initObservers(){
        viewModel.salesSearchResultsData.observe(this){
            searchResultsList.apply {
                clear()
                addAll(it)
            }

            salesAdapter.notifyDataSetChanged()
            isLoading(false)
        }

        viewModel.errorData.observe(this){
            Toast.makeText(requireActivity(), "Xatolik!", Toast.LENGTH_SHORT).show()
            isLoading(false)
        }
    }

    private fun isLoading(bool: Boolean) {
        if (bool) {
            binding.progressBar.visibility = View.VISIBLE
            binding.searchResultRv.visibility = View.GONE
            binding.noResultsTitle.visibility = View.GONE
        } else {
            if (searchResultsList.isEmpty()){
                binding.noResultsTitle.visibility = View.VISIBLE
            }else{
                binding.searchResultRv.visibility = View.VISIBLE
                binding.noResultsTitle.visibility = View.GONE
            }

            binding.progressBar.visibility = View.GONE
        }
    }

    override fun itemSelectedListener(position: Int) {
        findNavController().navigate(R.id.action_searchFragment_to_trafficsFragment, bundleOf(
            "selectedSales" to searchResultsList[position]
        ))
    }

}