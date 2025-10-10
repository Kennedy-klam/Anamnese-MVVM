package com.kennedy.cadastrofirestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kennedy.cadastrofirestore.databinding.ActivityMainBinding
import com.kennedy.cadastrofirestore.ui.StepsPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = StepsPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Desabilita o gesto de deslizar para forçar o uso dos botões
        binding.viewPager.isUserInputEnabled = false
    }

    // Métodos públicos para os fragments controlarem a navegação
    fun navigateToNextPage() {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    fun navigateToPreviousPage() {
        binding.viewPager.currentItem = binding.viewPager.currentItem - 1
    }
}