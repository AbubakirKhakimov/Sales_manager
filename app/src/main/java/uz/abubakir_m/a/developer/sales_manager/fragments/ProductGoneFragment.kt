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
import uz.abubakir_m.a.developer.sales_manager.adapters.GoneAdapter
import uz.abubakir_m.a.developer.sales_manager.databinding.AddArrivedDialogLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.databinding.AddGoneDialogLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentProductGoneBinding
import uz.abubakir_m.a.developer.sales_manager.models.Arrived
import uz.abubakir_m.a.developer.sales_manager.models.DatabaseRef
import uz.abubakir_m.a.developer.sales_manager.models.Gone
import uz.abubakir_m.a.developer.sales_manager.models.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductGoneFragment : Fragment() {

    lateinit var binding: FragmentProductGoneBinding
    lateinit var viewModel: MainViewModel
    lateinit var goneAdapter: GoneAdapter
    val goneList = ArrayList<Gone>()

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
        binding = FragmentProductGoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goneAdapter = GoneAdapter(goneList)
        binding.goneRv.adapter = goneAdapter
        loadData()

        binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.add.setOnClickListener {
            showAddGoneDialog()
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_productGoneFragment_to_searchFragment, bundleOf(
                "loadWhere" to 1
            ))
        }

    }

    private fun loadData(){
        binding.swipeRefresh.isRefreshing = true
        viewModel.loadGone()
    }

    private fun initObservers(){
        viewModel.goneData.observe(this){
            goneList.apply {
                clear()
                addAll(it)
            }
            goneAdapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.errorData.observe(this){
            Toast.makeText(requireActivity(), "Xatolik!", Toast.LENGTH_SHORT).show()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showAddGoneDialog(){
        var date = Date().time
        var name = ""
        var salary = 0.0
        var flour = 0.0
        var bran = 0.0
        var porridge = 0.0
        var rice = 0.0

        val customDialog = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = AddGoneDialogLayoutBinding.inflate(layoutInflater)
        customDialog.setView(dialogBinding.root)

        dialogBinding.day.setText(getStringDate("dd", date))
        dialogBinding.month.setText(getStringDate("MM", date))
        dialogBinding.year.setText(getStringDate("yyyy", date))

        dialogBinding.cancel.setOnClickListener {
            customDialog.dismiss()
        }

        dialogBinding.save.setOnClickListener {
            name = dialogBinding.name.text.toString().trim()
            salary = dialogBinding.salary.text.toString().trim().toDoubleOrNull() ?: 0.0
            flour = dialogBinding.flour.text.toString().trim().toDoubleOrNull() ?: 0.0
            bran = dialogBinding.bran.text.toString().trim().toDoubleOrNull() ?: 0.0
            porridge = dialogBinding.porridge.text.toString().toDoubleOrNull() ?: 0.0
            rice = dialogBinding.rice.text.toString().trim().toDoubleOrNull() ?: 0.0

            if (name.isEmpty() || salary == 0.0 || (flour == 0.0 && bran == 0.0 && porridge == 0.0 && rice == 0.0)){
                Toast.makeText(requireActivity(), "Iltimos barcha kataklarni to'ldiring!", Toast.LENGTH_SHORT).show()
            }else{
                val dbRef = DatabaseRef.goneRef.push()
                val gone = Gone(
                    dbRef.key!!,
                    date,
                    name,
                    flour,
                    bran,
                    porridge,
                    rice,
                    salary
                )

                isLoading(true, dialogBinding)
                dbRef.setValue(gone).addOnCompleteListener {
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

    private fun isLoading(bool: Boolean, dialogBinding: AddGoneDialogLayoutBinding) {
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