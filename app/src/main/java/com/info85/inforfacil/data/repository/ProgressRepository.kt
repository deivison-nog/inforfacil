package com.info85.inforfacil.data.repository

import com.info85.inforfacil.data.local.Configuracoes
import com.info85.inforfacil.data.local.ModuloProgress
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.local.ProgressModel
import com.info85.inforfacil.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProgressRepository(private val dataStore: ProgressDataStore) {

    val progressFlow: Flow<ProgressModel> = dataStore.progressFlow

    val progressComModulosDefault: Flow<ProgressModel> = dataStore.progressFlow.map { progress ->
        val modulosCompletos = Constants.MODULE_IDS.associateWith { id ->
            progress.modulos[id] ?: ModuloProgress(moduloId = id)
        }
        val atualizado = progress.copy(
            modulos = modulosCompletos,
            totalEstrelas = calcularTotalEstrelas(modulosCompletos),
            percentualGeral = calcularPercentualGeral(modulosCompletos)
        )
        atualizado.copy(conquistasDesbloqueadas = calcularConquistas(atualizado))
    }

    suspend fun salvarProgressoInicial() {
        val modulosDefault = Constants.MODULE_IDS.associateWith { id ->
            ModuloProgress(moduloId = id)
        }
        val progressoInicial = ProgressModel(modulos = modulosDefault)
        dataStore.saveProgress(progressoInicial)
    }

    suspend fun atualizarModulo(moduloId: String, moduloProgress: ModuloProgress) {
        val atual = progressComModulosDefault.first()
        val atualizadoModulos = atual.modulos.toMutableMap().apply {
            this[moduloId] = moduloProgress
        }

        val hoje = currentDateString()
        val diasConsecutivosAtualizados = calcularDiasConsecutivos(atual.ultimaAtividadeData, atual.diasConsecutivos, hoje)

        val atualizado = atual.copy(
            modulos = atualizadoModulos,
            totalEstrelas = calcularTotalEstrelas(atualizadoModulos),
            percentualGeral = calcularPercentualGeral(atualizadoModulos),
            diasConsecutivos = diasConsecutivosAtualizados,
            ultimaAtividadeData = hoje
        )

        val comConquistas = atualizado.copy(
            conquistasDesbloqueadas = calcularConquistas(atualizado)
        )

        dataStore.saveProgress(comConquistas)
    }

    suspend fun atualizarConfiguracoes(configuracoes: Configuracoes) {
        dataStore.updateConfiguracoes(configuracoes)
    }

    suspend fun atualizarFavoritosGlossario(favoritos: Set<String>) {
        val atual = progressComModulosDefault.first()
        dataStore.saveProgress(atual.copy(glossarioFavoritos = favoritos))
    }

    suspend fun limparProgresso() {
        dataStore.clearProgress()
    }

    suspend fun exportarProgressoJson(): String = dataStore.exportProgressJson()

    suspend fun importarProgressoJson(json: String): Boolean = dataStore.importProgressJson(json)

    fun calcularPercentualGeral(modulos: Map<String, ModuloProgress>): Float {
        if (modulos.isEmpty()) return 0f
        return modulos.values.map { it.percentualConcluido }.average().toFloat()
    }

    fun calcularTotalEstrelas(modulos: Map<String, ModuloProgress>): Int {
        return modulos.values.sumOf { it.estrelas }
    }

    private fun calcularConquistas(progress: ProgressModel): Map<String, String> {
        val desbloqueadas = progress.conquistasDesbloqueadas.toMutableMap()
        val hoje = currentDateString()
        val modulosConcluidos = progress.modulos.values.count { it.concluido }
        val modulosCom3Estrelas = progress.modulos.values.count { it.estrelas == 3 }

        unlockIf(desbloqueadas, "primeiro_passo", modulosConcluidos >= 1, hoje)
        unlockIf(desbloqueadas, "aprendiz_tecnologia", modulosConcluidos >= 3, hoje)
        unlockIf(desbloqueadas, "explorador_digital", modulosConcluidos >= 6, hoje)
        unlockIf(desbloqueadas, "mestre_informatica", modulosConcluidos >= 12, hoje)
        unlockIf(desbloqueadas, "estrela_brilhante", modulosCom3Estrelas >= 5, hoje)
        unlockIf(desbloqueadas, "persistente", progress.diasConsecutivos >= 7, hoje)
        unlockIf(desbloqueadas, "colecionador", progress.totalEstrelas >= 20, hoje)

        val email3 = progress.modulos["email"]?.estrelas == 3
        val seguranca3 = progress.modulos["seguranca"]?.estrelas == 3
        val teclado3 = progress.modulos["teclado"]?.estrelas == 3

        unlockIf(desbloqueadas, "especialista_email", email3, hoje)
        unlockIf(desbloqueadas, "navegador_seguro", seguranca3, hoje)
        unlockIf(desbloqueadas, "mestre_teclado", teclado3, hoje)

        return desbloqueadas
    }

    private fun unlockIf(
        unlocked: MutableMap<String, String>,
        id: String,
        shouldUnlock: Boolean,
        date: String
    ) {
        if (shouldUnlock && !unlocked.containsKey(id)) {
            unlocked[id] = date
        }
    }

    private fun currentDateString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun calcularDiasConsecutivos(
        ultimaData: String?,
        streakAtual: Int,
        hoje: String
    ): Int {
        if (ultimaData.isNullOrBlank()) return 1
        if (ultimaData == hoje) return streakAtual.coerceAtLeast(1)

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val ultima = format.parse(ultimaData) ?: return 1
            val hojeDate = format.parse(hoje) ?: return 1

            val calUltima = Calendar.getInstance().apply { time = ultima }
            val calHoje = Calendar.getInstance().apply { time = hojeDate }

            val diffMillis = calHoje.timeInMillis - calUltima.timeInMillis
            val dias = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

            when (dias) {
                0 -> streakAtual.coerceAtLeast(1)
                1 -> streakAtual + 1
                else -> 1
            }
        } catch (_: Exception) {
            1
        }
    }
}
