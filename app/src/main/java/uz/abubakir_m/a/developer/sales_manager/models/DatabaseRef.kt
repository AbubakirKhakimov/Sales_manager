package uz.abubakir_m.a.developer.sales_manager.models

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object DatabaseRef {
    val rootRef = Firebase.database.reference
    val salesRef = Firebase.database.getReference(Keys.SALES_KEY)
}