package uz.abubakir_m.a.developer.sales_manager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.adapters.SalesAdapter
import uz.abubakir_m.a.developer.sales_manager.adapters.SalesCallBack
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentProductBinding
import uz.abubakir_m.a.developer.sales_manager.models.MainViewModel
import uz.abubakir_m.a.developer.sales_manager.models.Sales

class ProductFragment : Fragment(), SalesCallBack {

    lateinit var binding: FragmentProductBinding
    val salesList = ArrayList<Sales>()
    lateinit var viewModel: MainViewModel
    lateinit var arrivedAdapter: SalesAdapter

    companion object{
        val loadingData = MutableLiveData<Boolean>()

        fun loadingData(data:Boolean){
            loadingData.value = data
        }
    }

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
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrivedAdapter = SalesAdapter(salesList, this)
        binding.arrivedRv.adapter = arrivedAdapter
        loadData()

        binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_productArrivedFragment_to_searchFragment)
        }

        binding.arrived.setOnClickListener {
            AddArrivedDialogFragment().show(requireActivity().supportFragmentManager, "tag")
        }

        binding.gone.setOnClickListener {
            AddGoneDialogFragment().show(requireActivity().supportFragmentManager, "tag")
        }

    }

    private fun loadData(){
        binding.swipeRefresh.isRefreshing = true
        viewModel.loadSales()
    }

    private fun initObservers(){
        viewModel.salesData.observe(this){
            salesList.clear()
            salesList.addAll(it)
            arrivedAdapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.errorData.observe(this){
            Toast.makeText(requireActivity(), "Xatolik!", Toast.LENGTH_SHORT).show()
            binding.swipeRefresh.isRefreshing = false
        }

        loadingData.observe(this) {
            if (it) {
                loadData()
            }
        }
    }

    override fun itemSelectedListener(position: Int) {
        findNavController().navigate(R.id.action_productFragment_to_trafficsFragment, bundleOf(
            "selectedSales" to salesList[position]
        ))
    }

}