package com.kennedy.cadastrofirestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kennedy.cadastrofirestore.MainActivity
import com.kennedy.cadastrofirestore.databinding.FragmentStepTwoBinding

class StepTwoFragment : Fragment() {

    private var _binding: FragmentStepTwoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            (activity as? MainActivity)?.navigateToNextPage()
        }

        binding.buttonBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousPage()
        }

        binding.editTextPhone.doOnTextChanged { text, _, _, _ ->
            viewModel.updateStepTwoData(text.toString(), binding.editTextAddress.text.toString())
        }
        binding.editTextAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.updateStepTwoData(binding.editTextPhone.text.toString(), text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}