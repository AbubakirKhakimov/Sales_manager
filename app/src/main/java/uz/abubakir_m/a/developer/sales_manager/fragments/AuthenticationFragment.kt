package uz.abubakir_m.a.developer.sales_manager.fragments

import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentAuthenticationBinding


class AuthenticationFragment : Fragment() {

    lateinit var binding: FragmentAuthenticationBinding
    var password = ""
    val vibrator: Vibrator by lazy {
        requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getAppPassword() == null){
            findNavController().navigate(R.id.action_authenticationFragment_to_homeFragment)
        }

        binding.one.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.two.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.three.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.four.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.five.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.six.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.seven.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.eight.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.nine.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.zero.setOnClickListener {
            updateUI((it as TextView).text.toString())
        }

        binding.delete.setOnClickListener {
            if (password.isNotEmpty()) {
                password = password.substring(0, password.length - 1)
                binding.preview.text = password
            }
        }

    }

    private fun updateUI(selectedNum:String){
        password += selectedNum

        if (password.length == 4){
            if (getAppPassword() == password){
                findNavController().navigate(R.id.action_authenticationFragment_to_homeFragment)
            }else{
                vibrate()
            }

            password = ""
        }

        binding.preview.text = password
    }

    private fun vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            vibrator.vibrate(300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = Color.parseColor("#7E7153")
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.statusBarColor = Color.parseColor("#E8BA49")
    }

    private fun getAppPassword():String?{
        return Hawk.get("appPassword", null)
    }

}