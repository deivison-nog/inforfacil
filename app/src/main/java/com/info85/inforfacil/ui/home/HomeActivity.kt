package com.info85.inforfacil.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.info85.inforfacil.databinding.ActivityHomeBinding
import com.info85.inforfacil.models.ModuleItem
import com.info85.inforfacil.ui.achievements.AchievementsActivity
import com.info85.inforfacil.ui.glossary.GlossaryActivity
import com.info85.inforfacil.ui.settings.SettingsActivity
import com.info85.inforfacil.utils.showToast
import com.info85.inforfacil.utils.toPercent

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var modulosAdapter: ModulosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        modulosAdapter = ModulosAdapter { modulo ->
            onModuloClick(modulo)
        }
        binding.rvModulos.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = modulosAdapter
        }
    }

    private fun setupObservers() {
        viewModel.progress.observe(this) { progress ->
            binding.tvSaudacao.text = "Olá, ${progress.configuracoes.nomeUsuario}!"
            binding.progressBarGeral.progress = (progress.percentualGeral * 100).toInt()
            binding.tvPercentualGeral.text = progress.percentualGeral.toPercent()
            binding.tvTotalEstrelas.text = "${progress.totalEstrelas} ⭐"
        }

        viewModel.modulosItems.observe(this) { modulos ->
            modulosAdapter.submitList(modulos)
        }
    }

    private fun setupClickListeners() {
        binding.btnGlossario.setOnClickListener {
            startActivity(Intent(this, GlossaryActivity::class.java))
        }
        binding.btnConquistas.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }
        binding.btnConfiguracoes.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun onModuloClick(modulo: ModuleItem) {
        // TODO: Navegar para o módulo específico em versão futura
        showToast("${modulo.nome} em desenvolvimento")
    }
}
