package com.kennedy.cadastrofirestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kennedy.cadastrofirestore.MainActivity
import com.kennedy.cadastrofirestore.databinding.FragmentStepOneBinding

class StepOneFragment : Fragment() {

    private var _binding: FragmentStepOneBinding? = null
    private val binding get() = _binding!!

    // Pega o ViewModel compartilhado da Activity
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener para o botão "Próximo"
        binding.buttonNext.setOnClickListener {
            // Chama um método na MainActivity para ir para a próxima página
            (activity as? MainActivity)?.navigateToNextPage()
        }

        // Atualiza o ViewModel enquanto o usuário digita
        binding.editTextName.doOnTextChanged { text, _, _, _ ->
            viewModel.updateStepOneData(text.toString(), binding.editTextEmail.text.toString())
        }
        binding.editTextEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.updateStepOneData(binding.editTextName.text.toString(), text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}