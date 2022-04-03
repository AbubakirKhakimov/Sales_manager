package uz.abubakir_m.a.developer.sales_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abubakir_m.a.developer.sales_manager.databinding.GoneItemLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.models.Gone
import java.text.SimpleDateFormat
import java.util.*

class GoneAdapter (val goneList:ArrayList<Gone>): RecyclerView.Adapter<GoneAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: GoneItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(GoneItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = goneList[position]
        holder.binding.date.text = getStringDate(item.date)
        holder.binding.name.text = item.name
        holder.binding.salary.text = getConcatenateString("Narxi:",item.salary, false)

        holder.binding.flour.text = getConcatenateString("Un:",item.flour)
        holder.binding.bran.text = getConcatenateString("Kepak:",item.bran)
        holder.binding.porridge.text = getConcatenateString("Yorma:",item.porridge)
        holder.binding.rice.text = getConcatenateString("Guruch:",item.rice)
    }

    override fun getItemCount(): Int {
        return goneList.size
    }

    private fun getStringDate(it: Long):String{
        return SimpleDateFormat("dd.MM.yyyy").format(Date(it))
    }

    private fun getConcatenateString(pattern:String, number:Double, weight:Boolean = true):String{
        return "$pattern ${String.format("%.2f", number)} ${if (weight) "kg" else "so'm"}"
    }
}