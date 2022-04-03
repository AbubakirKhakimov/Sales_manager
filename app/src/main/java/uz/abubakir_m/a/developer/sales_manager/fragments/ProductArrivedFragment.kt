package uz.abubakir_m.a.developer.sales_manager.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.adapters.ArrivedAdapter
import uz.abubakir_m.a.developer.sales_manager.databinding.AddArrivedDialogLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentProductArrivedBinding
import uz.abubakir_m.a.developer.sales_manager.models.Arrived
import uz.abubakir_m.a.developer.sales_manager.models.DatabaseRef
import uz.abubakir_m.a.developer.sales_manager.models.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductArrivedFragment : Fragment() {

    lateinit var binding: FragmentProductArrivedBinding
    val arrivedList = ArrayList<Arrived>()
    lateinit var viewModel:MainViewModel
    lateinit var arrivedAdapter: ArrivedAdapter

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
        binding = FragmentProductArrivedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrivedAdapter = ArrivedAdapter(arrivedList)
        binding.arrivedRv.adapter = arrivedAdapter
        loadData()

        binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.add.setOnClickListener {
            showAddArrivedDialog()
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_productArrivedFragment_to_searchFragment, bundleOf(
                "loadWhere" to 0
            ))
        }

    }

    private fun loadData(){
        binding.swipeRefresh.isRefreshing = true
        viewModel.loadArrived()
    }

    private fun initObservers(){
        viewModel.arrivedData.observe(this){
            arrivedList.clear()
            arrivedList.addAll(it)
            arrivedAdapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.errorData.observe(this){
            Toast.makeText(requireActivity(), "Xatolik!", Toast.LENGTH_SHORT).show()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showAddArrivedDialog(){
        var date = Date().time
        var flour = 0.0
        var bran = 0.0
        var name = ""
        var wheat = 0.0

        val customDialog = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = AddArrivedDialogLayoutBinding.inflate(layoutInflater)
        customDialog.setView(dialogBinding.root)

        dialogBinding.day.setText(getStringDate("dd", date))
        dialogBinding.month.setText(getStringDate("MM", date))
        dialogBinding.year.setText(getStringDate("yyyy", date))

        dialogBinding.wheat.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                wheat = it.toString().toDouble()
                flour = wheat * 0.65
                bran = wheat * 0.2
            }else{
                wheat = 0.0
                flour = 0.0
                bran = 0.0
            }

            dialogBinding.flour.text = getConcatenateString("Un:", flour)
            dialogBinding.bran.text = getConcatenateString("Kepak:", bran)
        }

        dialogBinding.cancel.setOnClickListener {
            customDialog.dismiss()
        }

        dialogBinding.save.setOnClickListener {
            name = dialogBinding.name.text.toString().trim()
            if (name.isEmpty() || flour == 0.0 || bran == 0.0 || wheat == 0.0){
                Toast.makeText(requireActivity(), "Iltimos barcha kataklarni to'ldiring!", Toast.LENGTH_SHORT).show()
            }else{
                val dbRef = DatabaseRef.arrivedRef.push()
                val arrived = Arrived(
                    dbRef.key!!,
                    date,
                    name,
                    wheat,
                    flour,
                    bran
                )

                isLoading(true, dialogBinding)
                dbRef.setValue(arrived).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(requireActivity(), "Muvaffaqiyatli saqlandi!", Toast.LENGTH_SHORT).show()
                        customDialog.dismiss()
                        loadData()
                    }else{
                        Toast.makeText(requireActivity(), "Xatolik!", Toast.LENGTH_SHORT).show()
                        isLoading(false, dialogBinding)
                    }
                }
            }
        }

        dialogBinding.showCalendar.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker().build()

            picker.addOnPositiveButtonClickListener {
                date = it
                dialogBinding.day.setText(getStringDate("dd", it))
                dialogBinding.month.setText(getStringDate("MM", it))
                dialogBinding.year.setText(getStringDate("yyyy", it))
            }

            picker.show(requireActivity().supportFragmentManager, "tag_picker")
        }

        customDialog.show()
    }

    private fun getStringDate(pattern:String, it: Long):String{
        return SimpleDateFormat(pattern).format(Date(it))
    }

    private fun getConcatenateString(pattern:String, number:Double):String{
        return "$pattern ${String.format("%.2f", number)} kg"
    }

    private fun isLoading(bool: Boolean, dialogBinding: AddArrivedDialogLayoutBinding) {
        if (bool) {
            dialogBinding.save.visibility = View.INVISIBLE
            dialogBinding.progressBar.visibility = View.VISIBLE
            dialogBinding.cancel.isClickable = false
        } else {
            dialogBinding.save.visibility = View.VISIBLE
            dialogBinding.progressBar.visibility = View.INVISIBLE
            dialogBinding.cancel.isClickable = true
        }
    }

}