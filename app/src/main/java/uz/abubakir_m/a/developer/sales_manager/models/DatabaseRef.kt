package uz.abubakir_m.a.developer.sales_manager.models

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object DatabaseRef {
    val rootRef = Firebase.database.reference
    val arrivedRef = Firebase.database.getReference(Keys.ARRIVED_KEY)
    val goneRef = Firebase.database.getReference(Keys.GONE_KEY)
}