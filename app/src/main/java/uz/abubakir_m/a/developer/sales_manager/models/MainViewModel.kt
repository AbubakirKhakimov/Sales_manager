package uz.abubakir_m.a.developer.sales_manager.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainViewModel:ViewModel() {
    val arrivedData = MutableLiveData<ArrayList<Arrived>>()
    val goneData = MutableLiveData<ArrayList<Gone>>()
    val arrivedSearchResultsData = MutableLiveData<ArrayList<Arrived>>()
    val goneSearchResultsData = MutableLiveData<ArrayList<Gone>>()
    val errorData = MutableLiveData<String>()

    fun loadArrived(){
        DatabaseRef.arrivedRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Arrived>()

                for (item in snapshot.children){
                    list.add(item.getValue(Arrived::class.java)!!)
                }
                list.reverse()

                arrivedData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                errorData.value = error.message
            }
        })
    }

    fun loadGone(){
        DatabaseRef.goneRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Gone>()

                for (item in snapshot.children){
                    list.add(item.getValue(Gone::class.java)!!)
                }
                list.reverse()

                goneData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                errorData.value = error.message
            }
        })
    }

    fun loadArrivedSearchResults(searchingText: String){
        val query = DatabaseRef.arrivedRef.orderByChild("name").equalTo(searchingText)
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Arrived>()

                for (item in snapshot.children){
                    list.add(item.getValue(Arrived::class.java)!!)
                }
                list.reverse()

                arrivedSearchResultsData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                errorData.value = error.message
            }
        })
    }

    fun loadGoneSearchResults(searchingText: String){
        val query = DatabaseRef.goneRef.orderByChild("name").equalTo(searchingText)
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Gone>()

                for (item in snapshot.children){
                    list.add(item.getValue(Gone::class.java)!!)
                }
                list.reverse()

                goneSearchResultsData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                errorData.value = error.message
            }
        })
    }

}