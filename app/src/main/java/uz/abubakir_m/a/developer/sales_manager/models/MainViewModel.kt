package uz.abubakir_m.a.developer.sales_manager.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainViewModel:ViewModel() {
    val salesData = MutableLiveData<ArrayList<Sales>>()
    val salesSearchResultsData = MutableLiveData<ArrayList<Sales>>()
    val writeIsSuccessful = MutableLiveData<String>()
    val errorData = MutableLiveData<String>()

    fun loadSales(){
        DatabaseRef.salesRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Sales>()

                for (item in snapshot.children){
                    list.add(item.getValue(Sales::class.java)!!)
                }
                list.reverse()

                salesData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                errorData.value = error.message
            }
        })
    }

    fun loadSalesSearchResults(searchingText: String){
        val query = DatabaseRef.salesRef.orderByChild("name").equalTo(searchingText)

        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Sales>()

                for (item in snapshot.children){
                    list.add(item.getValue(Sales::class.java)!!)
                }

                salesSearchResultsData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                errorData.value = error.message
            }
        })
    }

    fun writeFirebase(dbRef:DatabaseReference, item: Sales){
        dbRef.setValue(item).addOnCompleteListener {
            if (it.isSuccessful){
                writeIsSuccessful.value = "Successful!"
            }else{
                errorData.value = "Error!"
            }
        }
    }

}