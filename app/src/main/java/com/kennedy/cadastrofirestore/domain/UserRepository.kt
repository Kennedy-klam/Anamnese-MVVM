package com.kennedy.cadastrofirestore.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.kennedy.cadastrofirestore.data.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    // Pega a instância do Firestore
    private val db = FirebaseFirestore.getInstance()

    // Função para salvar o usuário. 'suspend' indica que é uma função de coroutine
    // que pode ser pausada e retomada, ideal para operações de rede/banco de dados.
    suspend fun saveUser(user: User) {
        try {
            // Acessa a coleção "users" e adiciona um novo documento com os dados do usuário.
            // O .await() espera a operação terminar sem bloquear a thread principal.
            db.collection("users").add(user).await()
        } catch (e: Exception) {
            // Se der um erro, a exceção será relançada para quem chamou (o ViewModel).
            throw e
        }
    }
}