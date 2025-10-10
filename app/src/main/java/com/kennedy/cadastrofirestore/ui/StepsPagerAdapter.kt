package com.kennedy.cadastrofirestore.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StepsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Temos 2 etapas
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StepOneFragment() // Posição 0 é a Etapa 1
            1 -> StepTwoFragment() // Posição 1 é a Etapa 2
            else -> throw IllegalStateException("Posição de fragmento inválida")
        }
    }
}