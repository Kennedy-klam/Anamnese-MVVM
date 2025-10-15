package com.kennedy.cadastrofirestore.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kennedy.cadastrofirestore.data.HealthHistory
import com.kennedy.cadastrofirestore.data.MentalState
import com.kennedy.cadastrofirestore.data.User
import com.kennedy.cadastrofirestore.domain.UserRepository
import kotlinx.coroutines.launch

sealed class SaveState {
    object Loading : SaveState()
    data class Success(val message: String) : SaveState()
    data class Error(val errorMessage: String) : SaveState()
}

class MainViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _saveState = MutableLiveData<SaveState>()
    val saveState: LiveData<SaveState> = _saveState

    private val _formData = MutableLiveData<User>(User())
    val formData: LiveData<User> = _formData

    fun saveUser() {
        val user = _formData.value ?: return

        // Adicione aqui validações mais robustas se necessário
        if (user.name.isBlank() || user.email.isBlank()) {
            _saveState.value = SaveState.Error("Por favor, preencha todos os campos das etapas anteriores.")
            return
        }

        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            try {
                userRepository.saveUser(user)
                _saveState.value = SaveState.Success("Usuário salvo com sucesso!")
            } catch (e: Exception) {
                _saveState.value = SaveState.Error("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun updateStepOneData(name: String, email: String) {
        val currentUser = _formData.value ?: User()
        _formData.value = currentUser.copy(name = name, email = email)
    }

    fun updateStepTwoData(phone: String, address: String) {
        val currentUser = _formData.value ?: User()
        _formData.value = currentUser.copy(phone = phone, address = address)
    }

    fun updateHealthHistory(newHistory: HealthHistory) {
        val currentUser = _formData.value ?: User()
        _formData.value = currentUser.copy(healthHistory = newHistory)
    }

    fun updateMentalState(newMentalState: MentalState) {
        val currentUser = _formData.value ?: User()
        val currentHealthHistory = currentUser.healthHistory
        _formData.value = currentUser.copy(
            healthHistory = currentHealthHistory.copy(mentalState = newMentalState)
        )
    }
}