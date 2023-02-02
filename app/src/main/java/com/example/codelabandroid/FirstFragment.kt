package com.example.codelabandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.codelabandroid.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val controlButtonEnabledWhenTextChanged = { _: CharSequence?, _: Int, _: Int, count: Int ->
        if (count == 0) {
            binding.buttonFirst.isEnabled = false
        } else if (areAllFieldsFilled()) {
            binding.buttonFirst.isEnabled = true
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        return binding.firstName.text.isNotBlank() && binding.lastName.text.isNotBlank()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstName.addTextChangedListener(onTextChanged = controlButtonEnabledWhenTextChanged)
        binding.lastName.addTextChangedListener(onTextChanged = controlButtonEnabledWhenTextChanged)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}