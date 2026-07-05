package com.info85.inforfacil.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.info85.inforfacil.data.local.Configuracoes
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivitySettingsBinding
import com.info85.inforfacil.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var repository: ProgressRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Configurações"

        repository = ProgressRepository(ProgressDataStore(applicationContext))

        setupActions()
        loadCurrentSettings()
    }

    private fun loadCurrentSettings() {
        lifecycleScope.launch {
            val progress = repository.progressComModulosDefault.first()
            val config = progress.configuracoes
            binding.etNomeUsuario.setText(config.nomeUsuario)
            binding.switchSom.isChecked = config.somAtivado
            binding.switchVibracao.isChecked = config.vibracaoAtivada
            binding.switchModoEscuro.isChecked = config.modoEscuro
            binding.switchAltoContraste.isChecked = config.altoContraste
            binding.switchBotoesGrandes.isChecked = config.botoesGrandes
            binding.spinnerTema.setText(config.tema, false)
            binding.spinnerFonte.setText(config.tamanhoFonte, false)
        }
    }

    private fun setupActions() {
        binding.btnSalvarConfiguracoes.setOnClickListener {
            lifecycleScope.launch {
                val config = Configuracoes(
                    nomeUsuario = binding.etNomeUsuario.text?.toString()?.ifBlank { "Estudante" } ?: "Estudante",
                    somAtivado = binding.switchSom.isChecked,
                    vibracaoAtivada = binding.switchVibracao.isChecked,
                    tamanhoFonte = binding.spinnerFonte.text?.toString()?.ifBlank { "medio" } ?: "medio",
                    modoEscuro = binding.switchModoEscuro.isChecked,
                    tema = binding.spinnerTema.text?.toString()?.ifBlank { "azul" } ?: "azul",
                    altoContraste = binding.switchAltoContraste.isChecked,
                    botoesGrandes = binding.switchBotoesGrandes.isChecked
                )

                repository.atualizarConfiguracoes(config)
                applyDarkMode(config.modoEscuro)
                showToast("Configurações salvas")
            }
        }

        binding.btnExportar.setOnClickListener {
            lifecycleScope.launch {
                val json = repository.exportarProgressoJson()
                val file = File(getExternalFilesDir(null), "inforfacil_progress.json")
                file.writeText(json)
                showToast("Exportado para: ${file.absolutePath}")
            }
        }

        binding.btnImportar.setOnClickListener {
            lifecycleScope.launch {
                val file = File(getExternalFilesDir(null), "inforfacil_progress.json")
                if (!file.exists()) {
                    showToast("Arquivo não encontrado para importação")
                    return@launch
                }
                val ok = repository.importarProgressoJson(file.readText())
                showToast(if (ok) "Progresso importado com sucesso" else "Falha ao importar JSON")
                if (ok) loadCurrentSettings()
            }
        }

        binding.btnResetar.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Resetar progresso")
                .setMessage("Tem certeza que deseja apagar todo o progresso?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Resetar") { _, _ ->
                    lifecycleScope.launch {
                        repository.limparProgresso()
                        showToast("Progresso resetado")
                        loadCurrentSettings()
                    }
                }
                .show()
        }

        binding.btnSobre.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Sobre")
                .setMessage("InfoFácil v1.0\nApp educativo de informática básica offline.")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun applyDarkMode(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
