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
        // Lógica de visibilidade condicional
        binding.rgUrinary.setOnCheckedChangeListener { _, id -> binding.etUrinaryDate.isVisible = id == R.id.rbUrinaryYes }
        binding.rgFecal.setOnCheckedChangeListener { _, id -> binding.etFecalDate.isVisible = id == R.id.rbFecalYes }
        binding.rgSleep.setOnCheckedChangeListener { _, id -> binding.etSleepDetails.isVisible = id == R.id.rbSleepDisorder }
        binding.rgOrthosis.setOnCheckedChangeListener { _, id -> binding.etOrthosisDetails.isVisible = id == R.id.rbOrthosisYes }
        binding.rgProsthesis.setOnCheckedChangeListener { _, id -> binding.etProsthesisDetails.isVisible = id == R.id.rbProsthesisYes }
        binding.rgFall.setOnCheckedChangeListener { _, id -> binding.tilFallCount.isVisible = id == R.id.rbFallYes }
        binding.rgSmoker.setOnCheckedChangeListener { _, id -> binding.etSmokerStoppedTime.isVisible = id == R.id.rbSmokerNo }
        binding.rgAlcoholic.setOnCheckedChangeListener { _, id -> binding.etAlcoholicStoppedTime.isVisible = id == R.id.rbAlcoholicNo }

        // Adiciona um listener genérico para atualizar o ViewModel em qualquer mudança
        val textWatcher = { _: CharSequence?, _: Int, _: Int, _: Int -> updateViewModel() }
        binding.autoCompleteVision.doOnTextChanged(textWatcher)
        binding.autoCompleteHearing.doOnTextChanged(textWatcher)
        binding.etUrinaryDate.doOnTextChanged(textWatcher)
        binding.etFecalDate.doOnTextChanged(textWatcher)
        binding.etSleepDetails.doOnTextChanged(textWatcher)
        binding.etOrthosisDetails.doOnTextChanged(textWatcher)
        binding.etProsthesisDetails.doOnTextChanged(textWatcher)
        binding.autoCompleteFallCount.doOnTextChanged(textWatcher)
        binding.etSmokerStoppedTime.doOnTextChanged(textWatcher)
        binding.etAlcoholicStoppedTime.doOnTextChanged(textWatcher)

        // Botões de Navegação
        binding.buttonNext.setOnClickListener {
            updateViewModel() // Garante que os dados sejam salvos no ViewModel
            (activity as? MainActivity)?.navigateToNextPage()
        }

        binding.buttonBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousPage()
        }
    }

    private fun updateViewModel() {
        val history = HealthHistory(
            vision = binding.autoCompleteVision.text.toString(),
            hearing = binding.autoCompleteHearing.text.toString(),

            urinaryIncontinence = binding.rbUrinaryYes.isChecked,
            urinaryIncontinenceDate = binding.etUrinaryDate.text.toString().takeIf { binding.etUrinaryDate.isVisible },

            fecalIncontinence = binding.rbFecalYes.isChecked,
            fecalIncontinenceDate = binding.etFecalDate.text.toString().takeIf { binding.etFecalDate.isVisible },

            sleep = if (binding.rbSleepNormal.isChecked) "Normal" else "Distúrbios",
            sleepDisturbanceDetails = binding.etSleepDetails.text.toString().takeIf { binding.etSleepDetails.isVisible },

            usesOrthosis = binding.rbOrthosisYes.isChecked,
            orthosisDetails = binding.etOrthosisDetails.text.toString().takeIf { binding.etOrthosisDetails.isVisible },

            usesProsthesis = binding.rbProsthesisYes.isChecked,
            prosthesisDetails = binding.etProsthesisDetails.text.toString().takeIf { binding.etProsthesisDetails.isVisible },

            hadFallLast12Months = binding.rbFallYes.isChecked,
            fallCount = binding.autoCompleteFallCount.text.toString().takeIf { binding.tilFallCount.isVisible },

            isSmoker = binding.rbSmokerYes.isChecked,
            smokingStoppedTime = binding.etSmokerStoppedTime.text.toString().takeIf { binding.etSmokerStoppedTime.isVisible },

            isAlcoholic = binding.rbAlcoholicYes.isChecked,
            alcoholStoppedTime = binding.etAlcoholicStoppedTime.text.toString().takeIf { binding.etAlcoholicStoppedTime.isVisible }
        )
        viewModel.updateHealthHistory(history)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}