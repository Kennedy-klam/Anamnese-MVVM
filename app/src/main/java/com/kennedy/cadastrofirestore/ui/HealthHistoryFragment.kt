package com.kennedy.cadastrofirestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kennedy.cadastrofirestore.MainActivity
import com.kennedy.cadastrofirestore.R
import com.kennedy.cadastrofirestore.data.HealthHistory
import com.kennedy.cadastrofirestore.databinding.FragmentHealthHistoryBinding

class HealthHistoryFragment : Fragment() {

    private var _binding: FragmentHealthHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHealthHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()
        setupListeners()
    }

    private fun setupDropdowns() {
        val visionHearingOptions = listOf("Normal", "Déficit c/ uso de corretor", "Déficit s/ uso de corretor", "Perda parcial", "Perda total")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, visionHearingOptions)
        binding.autoCompleteVision.setAdapter(adapter)
        binding.autoCompleteHearing.setAdapter(adapter)

        val fallCountOptions = listOf("1", "2", "3", "4", "5 ou mais")
        val fallAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, fallCountOptions)
        binding.autoCompleteFallCount.setAdapter(fallAdapter)
    }

    private fun setupListeners() {
        // --- Lógica de visibilidade condicional ATUALIZADA ---

        // Continência Urinária
        binding.rgUrinary.setOnCheckedChangeListener { _, checkedId ->
            binding.llUrinaryDate.isVisible = checkedId == R.id.rbUrinaryYes
            updateViewModel()
        }

        // Continência Fecal
        binding.rgFecal.setOnCheckedChangeListener { _, checkedId ->
            binding.llFecalDate.isVisible = checkedId == R.id.rbFecalYes
            updateViewModel()
        }

        // Sono
        binding.rgSleep.setOnCheckedChangeListener { _, checkedId ->
            binding.llSleepDetails.isVisible = checkedId == R.id.rbSleepDisorder
            updateViewModel()
        }

        // Órtese
        binding.rgOrthosis.setOnCheckedChangeListener { _, checkedId ->
            binding.llOrthosisDetails.isVisible = checkedId == R.id.rbOrthosisYes
            updateViewModel()
        }

        // Prótese
        binding.rgProsthesis.setOnCheckedChangeListener { _, checkedId ->
            binding.llProsthesisDetails.isVisible = checkedId == R.id.rbProsthesisYes
            updateViewModel()
        }

        // Queda (Já estava correto)
        binding.rgFall.setOnCheckedChangeListener { _, checkedId ->
            binding.llFallDetails.isVisible = checkedId == R.id.rbFallYes
            updateViewModel()
        }

        // Fuma
        binding.rgSmoker.setOnCheckedChangeListener { _, checkedId ->
            binding.llSmokerStoppedTime.isVisible = checkedId == R.id.rbSmokerNo
            updateViewModel()
        }

        // Etilista
        binding.rgAlcoholic.setOnCheckedChangeListener { _, checkedId ->
            binding.llAlcoholicStoppedTime.isVisible = checkedId == R.id.rbAlcoholicNo
            updateViewModel()
        }


        // --- Listeners para salvar o texto digitado (doOnTextChanged) ---
        // (Eles ainda leem do EditText, o que está correto)
        binding.autoCompleteVision.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.autoCompleteHearing.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etUrinaryDate.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etFecalDate.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etSleepDetails.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etOrthosisDetails.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etProsthesisDetails.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.autoCompleteFallCount.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etSmokerStoppedTime.doOnTextChanged { _, _, _, _ -> updateViewModel() }
        binding.etAlcoholicStoppedTime.doOnTextChanged { _, _, _, _ -> updateViewModel() }


        // --- Botões de Navegação ---
        binding.buttonNext.setOnClickListener {
            updateViewModel() // Garante que os dados sejam salvos no ViewModel
            (activity as? MainActivity)?.navigateToNextPage()
        }

        binding.buttonBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousPage()
        }
    }

    private fun updateViewModel() {
        // Pega todos os valores da UI e cria um novo objeto HealthHistory
        val history = HealthHistory(
            vision = binding.autoCompleteVision.text.toString(),
            hearing = binding.autoCompleteHearing.text.toString(),

            urinaryIncontinence = binding.rbUrinaryYes.isChecked,
            urinaryIncontinenceDate = binding.etUrinaryDate.text.toString().takeIf { binding.llUrinaryDate.isVisible },

            fecalIncontinence = binding.rbFecalYes.isChecked,
            fecalIncontinenceDate = binding.etFecalDate.text.toString().takeIf { binding.llFecalDate.isVisible },

            sleep = if (binding.rbSleepNormal.isChecked) "Normal" else "Distúrbios",
            sleepDisturbanceDetails = binding.etSleepDetails.text.toString().takeIf { binding.llSleepDetails.isVisible },

            usesOrthosis = binding.rbOrthosisYes.isChecked,
            orthosisDetails = binding.etOrthosisDetails.text.toString().takeIf { binding.llOrthosisDetails.isVisible },

            usesProsthesis = binding.rbProsthesisYes.isChecked,
            prosthesisDetails = binding.etProsthesisDetails.text.toString().takeIf { binding.llProsthesisDetails.isVisible },

            hadFallLast12Months = binding.rbFallYes.isChecked,
            fallCount = binding.autoCompleteFallCount.text.toString().takeIf { binding.llFallDetails.isVisible },

            isSmoker = binding.rbSmokerYes.isChecked,
            smokingStoppedTime = binding.etSmokerStoppedTime.text.toString().takeIf { binding.llSmokerStoppedTime.isVisible },

            isAlcoholic = binding.rbAlcoholicYes.isChecked,
            alcoholStoppedTime = binding.etAlcoholicStoppedTime.text.toString().takeIf { binding.llAlcoholicStoppedTime.isVisible }
        )
        viewModel.updateHealthHistory(history)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}