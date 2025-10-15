package com.kennedy.cadastrofirestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kennedy.cadastrofirestore.MainActivity
import com.kennedy.cadastrofirestore.R
import com.kennedy.cadastrofirestore.data.MentalState
import com.kennedy.cadastrofirestore.databinding.FragmentMentalStateBinding

class MentalStateFragment : Fragment() {

    private var _binding: FragmentMentalStateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var timeCheckBoxes: List<CheckBox>
    private lateinit var locationCheckBoxes: List<CheckBox>
    private lateinit var wordCheckBoxes: List<CheckBox>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMentalStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeCheckBoxLists()
        setupDropdowns()
        setupListeners()
        setupObservers() // Adicionamos o observador aqui
    }

    private fun initializeCheckBoxLists() {
        timeCheckBoxes = listOf(binding.cbTimeMonth, binding.cbTimeDay, binding.cbTimeYear, binding.cbTimeWeekDay, binding.cbTimeApproximate)
        locationCheckBoxes = listOf(binding.cbLocationState, binding.cbLocationCity, binding.cbLocationNeighborhood, binding.cbLocationPlace, binding.cbLocationCountry)
        wordCheckBoxes = listOf(binding.cbWordCar, binding.cbWordVase, binding.cbWordBrick)
    }

    private fun setupDropdowns() {
        val mathScoreOptions = (0..5).map { it.toString() }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mathScoreOptions)
        binding.autoCompleteMathScore.setAdapter(adapter)
    }

    private fun setupListeners() {
        timeCheckBoxes.forEach { it.setOnCheckedChangeListener { _, _ -> calculateScoresAndUpdateVm() } }
        locationCheckBoxes.forEach { it.setOnCheckedChangeListener { _, _ -> calculateScoresAndUpdateVm() } }
        wordCheckBoxes.forEach { it.setOnCheckedChangeListener { _, _ -> calculateScoresAndUpdateVm() } }

        binding.rgMath.setOnCheckedChangeListener { _, checkedId ->
            binding.tvMathInstructionYes.isVisible = checkedId == R.id.rbMathYes
            binding.tvMathInstructionNo.isVisible = checkedId == R.id.rbMathNo
            calculateScoresAndUpdateVm()
        }

        binding.autoCompleteMathScore.doOnTextChanged { _, _, _, _ -> calculateScoresAndUpdateVm() }

        binding.buttonBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousPage()
        }

        // AÇÃO DE SALVAR
        binding.buttonSave.setOnClickListener {
            calculateScoresAndUpdateVm() // Garante que os últimos dados sejam salvos
            viewModel.saveUser()
        }
    }

    private fun calculateScoresAndUpdateVm() {
        val timeScore = timeCheckBoxes.count { it.isChecked }
        binding.tvTimeScore.text = timeScore.toString()

        val locationScore = locationCheckBoxes.count { it.isChecked }
        binding.tvLocationScore.text = locationScore.toString()

        val wordsScore = wordCheckBoxes.count { it.isChecked }
        binding.tvWordsScore.text = wordsScore.toString()

        updateViewModel()
    }

    private fun updateViewModel() {
        // ... (Esta função continua igual, sem alterações)
        val mentalState = MentalState(
            timeMonth = binding.cbTimeMonth.isChecked,
            timeDay = binding.cbTimeDay.isChecked,
            timeYear = binding.cbTimeYear.isChecked,
            timeWeekDay = binding.cbTimeWeekDay.isChecked,
            timeApproximate = binding.cbTimeApproximate.isChecked,
            timeScore = binding.tvTimeScore.text.toString().toInt(),
            locationState = binding.cbLocationState.isChecked,
            locationCity = binding.cbLocationCity.isChecked,
            locationNeighborhood = binding.cbLocationNeighborhood.isChecked,
            locationPlace = binding.cbLocationPlace.isChecked,
            locationCountry = binding.cbLocationCountry.isChecked,
            locationScore = binding.tvLocationScore.text.toString().toInt(),
            wordCar = binding.cbWordCar.isChecked,
            wordVase = binding.cbWordVase.isChecked,
            wordBrick = binding.cbWordBrick.isChecked,
            wordsScore = binding.tvWordsScore.text.toString().toInt(),
            doesMath = when (binding.rgMath.checkedRadioButtonId) {
                R.id.rbMathYes -> true
                R.id.rbMathNo -> false
                else -> null
            },
            mathScore = binding.autoCompleteMathScore.text.toString().toIntOrNull() ?: 0
        )
        viewModel.updateMentalState(mentalState)
    }

    // NOVA FUNÇÃO PARA OBSERVAR O RESULTADO DO SALVAMENTO
    private fun setupObservers() {
        viewModel.saveState.observe(viewLifecycleOwner) { state ->
            // Oculta o teclado virtual, se estiver aberto
            // activity?.currentFocus?.let { view ->
            //     val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            //     imm?.hideSoftInputFromWindow(view.windowToken, 0)
            // }

            when (state) {
                is SaveState.Loading -> {
                    // Você pode adicionar um ProgressBar a este layout se quiser
                    // binding.progressBar.visibility = View.VISIBLE
                    binding.buttonSave.isEnabled = false
                    binding.buttonBack.isEnabled = false
                }
                is SaveState.Success -> {
                    // binding.progressBar.visibility = View.GONE
                    binding.buttonSave.isEnabled = true
                    binding.buttonBack.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                is SaveState.Error -> {
                    // binding.progressBar.visibility = View.GONE
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