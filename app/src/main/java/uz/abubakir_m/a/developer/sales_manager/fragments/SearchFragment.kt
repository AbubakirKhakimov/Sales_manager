package uz.abubakir_m.a.developer.sales_manager.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uz.abubakir_m.a.developer.sales_manager.adapters.ArrivedAdapter
import uz.abubakir_m.a.developer.sales_manager.adapters.GoneAdapter
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentSearchBinding
import uz.abubakir_m.a.developer.sales_manager.models.Arrived
import uz.abubakir_m.a.developer.sales_manager.models.Gone
import uz.abubakir_m.a.developer.sales_manager.models.MainViewModel

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: MainViewModel
    var loadWhere = -1

    lateinit var arrivedAdapter: ArrivedAdapter
    lateinit var goneAdapter: GoneAdapter
    var arrivedSearchResultsList = ArrayList<Arrived>()
    var goneSearchResultsList = ArrayList<Gone>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initObservers()
        loadWhere = arguments?.getInt("loadWhere")!!
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

        when (loadWhere) {
            0 -> {
                arrivedAdapter = ArrivedAdapter(arrivedSearchResultsList)
                binding.searchResultRv.adapter = arrivedAdapter
            }
            1 -> {
                goneAdapter = GoneAdapter(goneSearchResultsList)
                binding.searchResultRv.adapter = goneAdapter
            }
        }
        requestFocus()

        binding.searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                isLoading(true)
                when (loadWhere) {
                    0 -> {
                        viewModel.loadArrivedSearchResults(binding.searchView.text.toString().trim())
                    }
                    1 -> {
                        viewModel.loadGoneSearchResults(binding.searchView.text.toString().trim())
                    }
                }
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
        viewModel.arrivedSearchResultsData.observe(this){
            arrivedSearchResultsList.apply {
                clear()
                addAll(it)
            }

            arrivedAdapter.notifyDataSetChanged()
            isLoading(false)
        }

        viewModel.goneSearchResultsData.observe(this){
            goneSearchResultsList.apply {
                clear()
                addAll(it)
            }

            goneAdapter.notifyDataSetChanged()
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
            if ( if (loadWhere == 0) arrivedSearchResultsList.isEmpty() else goneSearchResultsList.isEmpty() ){
                binding.noResultsTitle.visibility = View.VISIBLE
            }else{
                binding.searchResultRv.visibility = View.VISIBLE
                binding.noResultsTitle.visibility = View.GONE
            }

            binding.progressBar.visibility = View.GONE
        }
    }

}