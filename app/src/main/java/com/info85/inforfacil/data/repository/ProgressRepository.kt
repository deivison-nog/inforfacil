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
        val anterior = atual.modulos[moduloId]

        // Progressão de nível: sobe de nível a cada conclusão após o nível atual
        val novoNivel = calcularNovoNivel(anterior, moduloProgress)
        val progressoFinal = moduloProgress.copy(nivel = novoNivel)

        val atualizadoModulos = atual.modulos.toMutableMap().apply {
            this[moduloId] = progressoFinal
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

    /**
     * Registra acesso diário do usuário atualizando o streak sem alterar progresso dos módulos.
     */
    suspend fun registrarAcessoDiario() {
        val atual = progressComModulosDefault.first()
        val hoje = currentDateString()
        if (atual.ultimaAtividadeData == hoje) return
        val novoStreak = calcularDiasConsecutivos(atual.ultimaAtividadeData, atual.diasConsecutivos, hoje)
        val atualizado = atual.copy(diasConsecutivos = novoStreak, ultimaAtividadeData = hoje)
        val comConquistas = atualizado.copy(conquistasDesbloqueadas = calcularConquistas(atualizado))
        dataStore.saveProgress(comConquistas)
    }

    fun calcularPercentualGeral(modulos: Map<String, ModuloProgress>): Float =
        calcularPercentualGeralStatic(modulos)

    fun calcularTotalEstrelas(modulos: Map<String, ModuloProgress>): Int =
        calcularTotalEstrelasStatic(modulos)

    /**
     * Calcula o nível do módulo: sobe 1 nível por conclusão acima do nível 1.
     * Nível máximo: 5.
     */
    fun calcularNovoNivel(anterior: ModuloProgress?, novo: ModuloProgress): Int =
        calcularNovoNivelStatic(anterior, novo)

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
        synchronized(dateFormat) {
            dateFormat.format(Date())
        }

    private fun calcularDiasConsecutivos(
        ultimaData: String?,
        streakAtual: Int,
        hoje: String
    ): Int = calcularDiasConsecutivosStatic(ultimaData, streakAtual, hoje)

    companion object {
        internal val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun calcularTotalEstrelasStatic(modulos: Map<String, ModuloProgress>): Int =
            modulos.values.sumOf { it.estrelas }

        fun calcularPercentualGeralStatic(modulos: Map<String, ModuloProgress>): Float {
            if (modulos.isEmpty()) return 0f
            return modulos.values.map { it.percentualConcluido }.average().toFloat()
        }

        fun calcularNovoNivelStatic(anterior: ModuloProgress?, novo: ModuloProgress): Int {
            if (!novo.concluido) return anterior?.nivel ?: 1
            val nivelAtual = anterior?.nivel ?: 1
            val jaConcluidoAntes = anterior?.concluido ?: false
            return if (jaConcluidoAntes) (nivelAtual + 1).coerceAtMost(5) else nivelAtual
        }

        fun calcularDiasConsecutivosStatic(
            ultimaData: String?,
            streakAtual: Int,
            hoje: String
        ): Int {
            if (ultimaData.isNullOrBlank()) return 1
            if (ultimaData == hoje) return streakAtual.coerceAtLeast(1)

            return try {
                val ultima = synchronized(dateFormat) { dateFormat.parse(ultimaData) } ?: return 1
                val hojeDate = synchronized(dateFormat) { dateFormat.parse(hoje) } ?: return 1

                val calUltima = java.util.Calendar.getInstance().apply { time = ultima }
                val calHoje = java.util.Calendar.getInstance().apply { time = hojeDate }

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
}
