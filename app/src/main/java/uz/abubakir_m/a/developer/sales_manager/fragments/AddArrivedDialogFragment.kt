package uz.abubakir_m.a.developer.sales_manager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentAddArrivedDialogBinding
import uz.abubakir_m.a.developer.sales_manager.models.DatabaseRef
import uz.abubakir_m.a.developer.sales_manager.models.MainViewModel
import uz.abubakir_m.a.developer.sales_manager.models.Sales
import uz.abubakir_m.a.developer.sales_manager.models.Traffic
import java.text.SimpleDateFormat
import java.util.*

class AddArrivedDialogFragment : DialogFragment() {

    lateinit var binding: FragmentAddArrivedDialogBinding
    lateinit var viewModel: MainViewModel

    var date = Date().time
    var flour = 0.0
    var bran = 0.0
    var name = ""
    var wheat = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initObservers()
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddArrivedDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.day.setText(getStringDate("dd", date))
        binding.month.setText(getStringDate("MM", date))
        binding.year.setText(getStringDate("yyyy", date))

        binding.wheat.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                wheat = it.toString().toDouble()
                flour = wheat * 0.65
                bran = wheat * 0.2
            }else{
                wheat = 0.0
                flour = 0.0
                bran = 0.0
            }

            binding.flour.text = getConcatenateString("Un:", flour)
            binding.bran.text = getConcatenateString("Kepak:", bran)
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.save.setOnClickListener {
            name = binding.name.text.toString().trim()
            if (name.isEmpty() || flour == 0.0 || bran == 0.0 || wheat == 0.0){
                Toast.makeText(requireActivity(), "Iltimos barcha kataklarni to'ldiring!", Toast.LENGTH_SHORT).show()
            }else{
                isLoading(true)
                viewModel.loadSalesSearchResults(name)
            }
        }

        binding.showCalendar.setOnClickListener {
            showCalendar()
        }

    }

    private fun initObservers(){
        viewModel.writeIsSuccessful.observe(this){
            Toast.makeText(requireActivity(), "Muvaffaqiyatli saqlandi!", Toast.LENGTH_SHORT).show()
            dismiss()
            ProductFragment.loadingData(true)
        }

        viewModel.errorData.observe(this){
            Toast.makeText(requireActivity(), "Xatolik!", Toast.LENGTH_SHORT).show()
            isLoading(false)
        }

        viewModel.salesSearchResultsData.observe(this){
            if (it.isEmpty()){
                addSales()
            } else {
                editSales(it[0])
            }
        }
    }

    private fun addSales(){
        val dbRef = DatabaseRef.salesRef.push()
        val sales = Sales(
            dbRef.key!!,
            name,
            wheat,
            flour,
            bran,
            arrayListOf(Traffic(date, wheat, flour, bran))
        )

        viewModel.writeFirebase(dbRef, sales)
    }

    private fun editSales(it:Sales){
        it.wheat += wheat
        it.flour += flour
        it.bran += bran
        it.salesList.add(Traffic(date, wheat, flour, bran))

        viewModel.writeFirebase(DatabaseRef.salesRef.child(it.id), it)
    }

    private fun showCalendar(){
        val picker = MaterialDatePicker.Builder.datePicker().build()

        picker.addOnPositiveButtonClickListener {
            date = it
            binding.day.setText(getStringDate("dd", it))
            binding.month.setText(getStringDate("MM", it))
            binding.year.setText(getStringDate("yyyy", it))
        }

        picker.show(requireActivity().supportFragmentManager, "tag_picker")
    }

    private fun getStringDate(pattern:String, it: Long):String{
        return SimpleDateFormat(pattern).format(Date(it))
    }

    private fun getConcatenateString(pattern:String, number:Double):String{
        return "$pattern ${String.format("%.2f", number)} kg"
    }

    private fun isLoading(bool: Boolean) {
        if (bool) {
            binding.save.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.cancel.isClickable = false
        } else {
            binding.save.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.cancel.isClickable = true
        }
    }

}