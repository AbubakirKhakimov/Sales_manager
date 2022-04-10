package uz.abubakir_m.a.developer.sales_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abubakir_m.a.developer.sales_manager.databinding.SalesItemLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.models.Sales
import java.text.SimpleDateFormat
import java.util.*

interface SalesCallBack{
    fun itemSelectedListener(position: Int)
}

class SalesAdapter(val salesList:ArrayList<Sales>, val salesCallBack: SalesCallBack)
    :RecyclerView.Adapter<SalesAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: SalesItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(SalesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = salesList[position]
        holder.binding.name.text = item.name
        holder.binding.wheat.text = getConcatenateString("Bug'doy:",item.wheat)
        holder.binding.flour.text = getConcatenateString("Un:",item.flour)
        holder.binding.bran.text = getConcatenateString("Kepak:",item.bran)

        holder.binding.root.setOnClickListener {
            salesCallBack.itemSelectedListener(position)
        }
    }

    override fun getItemCount(): Int {
        return salesList.size
    }

    private fun getConcatenateString(pattern:String, number:Double):String{
        return "$pattern ${String.format("%.2f", number)} kg"
    }
}