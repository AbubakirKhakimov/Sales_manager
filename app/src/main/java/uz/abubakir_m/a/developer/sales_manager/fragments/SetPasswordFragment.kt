package uz.abubakir_m.a.developer.sales_manager.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.orhanobut.hawk.Hawk
import uz.abubakir_m.a.developer.sales_manager.R
import uz.abubakir_m.a.developer.sales_manager.databinding.FragmentSetPasswordBinding

class SetPasswordFragment : DialogFragment() {

    lateinit var binding: FragmentSetPasswordBinding
    var oldPassword = ""
    var newPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(getAppPassword() == null){
            binding.oldPasswordLayout.visibility = View.GONE
            binding.oldPassword.setText("0000")
        }

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.save.setOnClickListener {
            if (checkPasswords()){
                Hawk.put("appPassword", newPassword)
                Toast.makeText(requireActivity(), "Muvaffaqiyatli saqlandi!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        binding.oldPassword.doOnTextChanged { text, start, before, count ->
            binding.oldPasswordLayout.error = null
        }

        binding.newPassword.doOnTextChanged { text, start, before, count ->
            binding.newPasswordLayout.error = null
        }

    }

    private fun checkPasswords():Boolean{
        oldPassword = binding.oldPassword.text.toString()
        newPassword = binding.newPassword.text.toString()

        when{
            oldPassword.length < 4 -> {
                binding.oldPasswordLayout.error = "Parol 4 sondan iborat bo'lsin!"
                return false
            }
            newPassword.length < 4 -> {
                binding.newPasswordLayout.error = "Parol 4 sondan iborat bo'lsin!"
                return false
            }
            oldPassword != getAppPassword() ?: return true -> {
                binding.oldPasswordLayout.error = "Eski parol noto'g'ri terildi!"
                return false
            }
        }

        return true
    }

    private fun getAppPassword():String?{
        return Hawk.get("appPassword", null)
    }

}