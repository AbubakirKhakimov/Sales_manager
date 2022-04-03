package uz.abubakir_m.a.developer.sales_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abubakir_m.a.developer.sales_manager.databinding.ArrivedItemLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.models.Arrived
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArrivedAdapter(val arrivedList:ArrayList<Arrived>):RecyclerView.Adapter<ArrivedAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: ArrivedItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(ArrivedItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = arrivedList[position]
        holder.binding.date.text = getStringDate(item.date)
        holder.binding.name.text = item.name
        holder.binding.wheat.text = getConcatenateString("Bug'doy:",item.wheat)
        holder.binding.flour.text = getConcatenateString("Un:",item.flour)
        holder.binding.bran.text = getConcatenateString("Kepak:",item.bran)
    }

    override fun getItemCount(): Int {
        return arrivedList.size
    }

    private fun getStringDate(it: Long):String{
        return SimpleDateFormat("dd.MM.yyyy").format(Date(it))
    }

    private fun getConcatenateString(pattern:String, number:Double):String{
        return "$pattern ${String.format("%.2f", number)} kg"
    }
}