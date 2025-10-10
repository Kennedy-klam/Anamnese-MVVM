package com.kennedy.cadastrofirestore.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kennedy.cadastrofirestore.data.User // Verifique se o import está correto
import com.kennedy.cadastrofirestore.domain.UserRepository
import kotlinx.coroutines.launch

sealed class SaveState {
    object Loading : SaveState()
    data class Success(val message: String) : SaveState()
    data class Error(val errorMessage: String) : SaveState()
}

class MainViewModel : ViewModel() {

    private val userRepository = UserRepository()

    // LiveData para o estado do salvamento (igual antes)
    private val _saveState = MutableLiveData<SaveState>()
    val saveState: LiveData<SaveState> = _saveState

    // NOVO: LiveData para guardar os dados do formulário entre as etapas
    private val _formData = MutableLiveData<User>(User())
    val formData: LiveData<User> = _formData

    // ATUALIZADO: A função agora pega os dados direto do _formData
    fun saveUser() {
        val user = _formData.value ?: return // Pega o usuário atual

        if (user.name.isBlank() || user.email.isBlank() || user.phone.isBlank() || user.address.isBlank()) {
            _saveState.value = SaveState.Error("Por favor, preencha todos os campos.")
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

    // NOVO: Funções para atualizar os dados a partir dos Fragments
    fun updateStepOneData(name: String, email: String) {
        val currentUser = _formData.value ?: User()
        _formData.value = currentUser.copy(name = name, email = email)
    }

    fun updateStepTwoData(phone: String, address: String) {
        val currentUser = _formData.value ?: User()
        _formData.value = currentUser.copy(phone = phone, address = address)
    }
}