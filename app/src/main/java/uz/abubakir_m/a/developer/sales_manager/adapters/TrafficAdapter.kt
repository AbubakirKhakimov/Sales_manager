package uz.abubakir_m.a.developer.sales_manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.databinding.TrafficItemLayoutBinding
import uz.abubakir_m.a.developer.sales_manager.models.Traffic
import java.text.SimpleDateFormat
import java.util.*

class TrafficAdapter(val trafficList: ArrayList<Traffic>): RecyclerView.Adapter<TrafficAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: TrafficItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(TrafficItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = trafficList[position]

        if (item.wheat == null){
            holder.binding.wheatLayout.visibility = View.GONE
            holder.binding.trafficImage.setImageResource(R.drawable.ic_baseline_file_upload_24)
        }else{
            holder.binding.wheatLayout.visibility = View.VISIBLE
            holder.binding.trafficImage.setImageResource(R.drawable.ic_baseline_file_download_24)
            holder.binding.wheat.text = getConcatenateString("Bug'doy:",item.wheat)
        }

        holder.binding.flour.text = getConcatenateString("Un:",item.flour)
        holder.binding.bran.text = getConcatenateString("Kepak:",item.bran)
        holder.binding.date.text = getStringDate(item.date)
    }

    override fun getItemCount(): Int {
        return trafficList.size
    }

    private fun getStringDate(it: Long):String{
        return SimpleDateFormat("dd.MM.yyyy").format(Date(it))
    }

    private fun getConcatenateString(pattern:String, number:Double):String{
        return "$pattern ${String.format("%.2f", number)} kg"
    }
}