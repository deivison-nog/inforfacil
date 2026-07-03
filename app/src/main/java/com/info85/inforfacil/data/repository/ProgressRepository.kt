package com.info85.inforfacil.data.repository

import com.info85.inforfacil.data.local.Configuracoes
import com.info85.inforfacil.data.local.ModuloProgress
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.local.ProgressModel
import com.info85.inforfacil.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepository(private val dataStore: ProgressDataStore) {

    val progressFlow: Flow<ProgressModel> = dataStore.progressFlow

    val progressComModulosDefault: Flow<ProgressModel> = dataStore.progressFlow.map { progress ->
        if (progress.modulos.isEmpty()) {
            val modulosDefault = Constants.MODULE_IDS.associateWith { id ->
                ModuloProgress(moduloId = id)
            }
            progress.copy(modulos = modulosDefault)
        } else {
            val modulosCompletos = Constants.MODULE_IDS.associateWith { id ->
                progress.modulos[id] ?: ModuloProgress(moduloId = id)
            }
            progress.copy(modulos = modulosCompletos)
        }
    }

    suspend fun salvarProgressoInicial() {
        val modulosDefault = Constants.MODULE_IDS.associateWith { id ->
            ModuloProgress(moduloId = id)
        }
        val progressoInicial = ProgressModel(modulos = modulosDefault)
        dataStore.saveProgress(progressoInicial)
    }

    suspend fun atualizarModulo(moduloId: String, moduloProgress: ModuloProgress) {
        dataStore.updateModuloProgress(moduloId, moduloProgress)
    }

    suspend fun atualizarConfiguracoes(configuracoes: Configuracoes) {
        dataStore.updateConfiguracoes(configuracoes)
    }

    suspend fun limparProgresso() {
        dataStore.clearProgress()
    }

    fun calcularPercentualGeral(modulos: Map<String, ModuloProgress>): Float {
        if (modulos.isEmpty()) return 0f
        return modulos.values.map { it.percentualConcluido }.average().toFloat()
    }

    fun calcularTotalEstrelas(modulos: Map<String, ModuloProgress>): Int {
        return modulos.values.sumOf { it.estrelas }
    }
}
