package uz.abubakir_m.a.developer.sales_manager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.adapters.TrafficAdapter
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentTrafficsBinding
import uz.abubakir_m.a.developer.sales_manager.models.Sales

class TrafficsFragment : Fragment() {

    lateinit var binding: FragmentTrafficsBinding
    lateinit var sales: Sales
    lateinit var trafficAdapter: TrafficAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sales = arguments?.getSerializable("selectedSales") as Sales
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTrafficsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = sales.name
        trafficAdapter = TrafficAdapter(sales.salesList)
        binding.trafficRv.adapter = trafficAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}