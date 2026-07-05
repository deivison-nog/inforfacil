package com.info85.inforfacil.ui.onboarding

import android.content.Intent
import android.os.Bundle
import com.info85.inforfacil.data.local.Configuracoes
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivityOnboardingBinding
import com.info85.inforfacil.ui.base.BaseActivity
import com.info85.inforfacil.ui.home.HomeActivity
import com.info85.inforfacil.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnComecar.setOnClickListener {
            val nome = binding.etNomeOnboarding.text?.toString()?.trim()?.ifBlank { "Estudante" } ?: "Estudante"
            saveNameAndProceed(nome)
        }
    }

    private fun saveNameAndProceed(nome: String) {
        val repository = ProgressRepository(ProgressDataStore(applicationContext))
        CoroutineScope(Dispatchers.IO).launch {
            repository.atualizarConfiguracoes(Configuracoes(nomeUsuario = nome))
            appPreferences.isFirstLaunch = false
        }
        showToast("Bem-vindo(a), $nome!")
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
