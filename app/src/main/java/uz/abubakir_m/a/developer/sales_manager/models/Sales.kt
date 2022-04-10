package uz.abubakir_m.a.developer.sales_manager.models

import java.io.Serializable

data class Sales(
    val id: String = "",
    val name: String = "",
    var wheat: Double = 0.0,
    var flour:Double = 0.0,
    var bran: Double = 0.0,
    val salesList:ArrayList<Traffic> = ArrayList()
):Serializable
