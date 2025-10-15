package com.kennedy.cadastrofirestore.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StepsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4 // ATUALIZADO
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StepOneFragment()
            1 -> StepTwoFragment()
            2 -> HealthHistoryFragment()
            3 -> MentalStateFragment() // NOVO
            else -> throw IllegalStateException("Posição de fragmento inválida")
        }
    }
}