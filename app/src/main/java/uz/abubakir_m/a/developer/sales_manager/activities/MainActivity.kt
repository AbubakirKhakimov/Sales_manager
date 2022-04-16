package uz.abubakir_m.a.developer.sales_manager.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import uz.abubakir_m.a.developer.sales_manager.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Hawk.init(newBase).build();
    }

}