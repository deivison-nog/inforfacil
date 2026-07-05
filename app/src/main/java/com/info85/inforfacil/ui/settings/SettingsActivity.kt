package com.info85.inforfacil.ui.settings

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.info85.inforfacil.data.local.Configuracoes
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivitySettingsBinding
import com.info85.inforfacil.ui.base.BaseActivity
import com.info85.inforfacil.utils.ThemeHelper
import com.info85.inforfacil.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var repository: ProgressRepository

    private val openDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@registerForActivityResult
        importFromUri(uri)
    }

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

                // Sincroniza com AppPreferences para aplicação síncrona do tema
                appPreferences.apply {
                    tema = config.tema
                    modoEscuro = config.modoEscuro
                    altoContraste = config.altoContraste
                    botoesGrandes = config.botoesGrandes
                    tamanhoFonte = config.tamanhoFonte
                    somAtivado = config.somAtivado
                    vibracaoAtivada = config.vibracaoAtivada
                }

                ThemeHelper.applyDarkMode(config.modoEscuro)
                showToast("Configurações salvas")
                recreate()
            }
        }

        binding.btnExportar.setOnClickListener {
            lifecycleScope.launch {
                runCatching {
                    val json = repository.exportarProgressoJson()
                    val file = File(getExternalFilesDir(null), "inforfacil_progress.json")
                    file.writeText(json)
                    file.absolutePath
                }.onSuccess { path ->
                    showToast("Exportado para: $path")
                }.onFailure {
                    showToast("Falha ao exportar o progresso")
                }
            }
        }

        binding.btnImportar.setOnClickListener {
            openDocumentLauncher.launch(arrayOf("application/json", "text/plain", "*/*"))
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

    private fun importFromUri(uri: Uri) {
        lifecycleScope.launch {
            runCatching {
                contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) }
                    ?: throw IllegalStateException("Não foi possível ler o arquivo")
            }.onSuccess { json ->
                val ok = repository.importarProgressoJson(json)
                showToast(if (ok) "Progresso importado com sucesso" else "Falha ao importar JSON")
                if (ok) loadCurrentSettings()
            }.onFailure {
                showToast("Falha ao importar o arquivo")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
