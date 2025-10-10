package com.kennedy.cadastrofirestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kennedy.cadastrofirestore.MainActivity
import com.kennedy.cadastrofirestore.databinding.FragmentStepTwoBinding
import com.kennedy.cadastrofirestore.ui.SaveState

class StepTwoFragment : Fragment() {

    private var _binding: FragmentStepTwoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            viewModel.saveUser() // Chama a função de salvar
        }

        binding.buttonBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousPage()
        }

        // Atualiza o ViewModel enquanto o usuário digita
        binding.editTextPhone.doOnTextChanged { text, _, _, _ ->
            viewModel.updateStepTwoData(text.toString(), binding.editTextAddress.text.toString())
        }
        binding.editTextAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.updateStepTwoData(binding.editTextPhone.text.toString(), text.toString())
        }

        // Observa o estado do salvamento (igual fazíamos na Activity)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.saveState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SaveState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.buttonSave.isEnabled = false
                    binding.buttonBack.isEnabled = false
                }
                is SaveState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonSave.isEnabled = true
                    binding.buttonBack.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is SaveState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonSave.isEnabled = true
                    binding.buttonBack.isEnabled = true
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}