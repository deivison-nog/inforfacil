package com.info85.inforfacil

import com.info85.inforfacil.data.local.ModuloProgress
import com.info85.inforfacil.data.repository.ProgressRepository
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Testes unitários para a lógica pura do ProgressRepository (companion object).
 * Não requerem Android SDK.
 */
class ProgressRepositoryTest {

    // -------------------------------------------------------------------------
    // calcularTotalEstrelasStatic
    // -------------------------------------------------------------------------

    @Test
    fun `totalEstrelas retorna zero quando mapa vazio`() {
        assertEquals(0, ProgressRepository.calcularTotalEstrelasStatic(emptyMap()))
    }

    @Test
    fun `totalEstrelas soma estrelas de todos modulos`() {
        val modulos = mapOf(
            "a" to ModuloProgress("a", estrelas = 3),
            "b" to ModuloProgress("b", estrelas = 2),
            "c" to ModuloProgress("c", estrelas = 0)
        )
        assertEquals(5, ProgressRepository.calcularTotalEstrelasStatic(modulos))
    }

    // -------------------------------------------------------------------------
    // calcularPercentualGeralStatic
    // -------------------------------------------------------------------------

    @Test
    fun `percentualGeral retorna zero quando mapa vazio`() {
        assertEquals(0f, ProgressRepository.calcularPercentualGeralStatic(emptyMap()), 0.001f)
    }

    @Test
    fun `percentualGeral calcula media dos percentuais`() {
        val modulos = mapOf(
            "a" to ModuloProgress("a", percentualConcluido = 1.0f),
            "b" to ModuloProgress("b", percentualConcluido = 0.5f)
        )
        assertEquals(0.75f, ProgressRepository.calcularPercentualGeralStatic(modulos), 0.001f)
    }

    @Test
    fun `percentualGeral com todos concluidos retorna 1`() {
        val modulos = mapOf(
            "a" to ModuloProgress("a", percentualConcluido = 1f),
            "b" to ModuloProgress("b", percentualConcluido = 1f)
        )
        assertEquals(1f, ProgressRepository.calcularPercentualGeralStatic(modulos), 0.001f)
    }

    // -------------------------------------------------------------------------
    // calcularNovoNivelStatic
    // -------------------------------------------------------------------------

    @Test
    fun `novoNivel nivel 1 quando concluido pela primeira vez`() {
        val anterior = ModuloProgress("m", nivel = 1, concluido = false)
        val novo = ModuloProgress("m", concluido = true)
        assertEquals(1, ProgressRepository.calcularNovoNivelStatic(anterior, novo))
    }

    @Test
    fun `novoNivel sobe nivel quando modulo ja era concluido`() {
        val anterior = ModuloProgress("m", nivel = 1, concluido = true)
        val novo = ModuloProgress("m", concluido = true)
        assertEquals(2, ProgressRepository.calcularNovoNivelStatic(anterior, novo))
    }

    @Test
    fun `novoNivel nivel maximo e 5`() {
        val anterior = ModuloProgress("m", nivel = 5, concluido = true)
        val novo = ModuloProgress("m", concluido = true)
        assertEquals(5, ProgressRepository.calcularNovoNivelStatic(anterior, novo))
    }

    @Test
    fun `novoNivel retorna nivel anterior quando nao concluido`() {
        val anterior = ModuloProgress("m", nivel = 3, concluido = false)
        val novo = ModuloProgress("m", concluido = false)
        assertEquals(3, ProgressRepository.calcularNovoNivelStatic(anterior, novo))
    }

    @Test
    fun `novoNivel retorna 1 quando anterior e null e concluido pela primeira vez`() {
        val novo = ModuloProgress("m", concluido = true)
        assertEquals(1, ProgressRepository.calcularNovoNivelStatic(null, novo))
    }

    // -------------------------------------------------------------------------
    // calcularDiasConsecutivosStatic
    // -------------------------------------------------------------------------

    @Test
    fun `streak retorna 1 quando ultimaData e nula`() {
        assertEquals(1, ProgressRepository.calcularDiasConsecutivosStatic(null, 5, "2026-07-05"))
    }

    @Test
    fun `streak retorna streak atual quando acesso no mesmo dia`() {
        assertEquals(3, ProgressRepository.calcularDiasConsecutivosStatic("2026-07-05", 3, "2026-07-05"))
    }

    @Test
    fun `streak incrementa quando acesso no dia seguinte`() {
        assertEquals(4, ProgressRepository.calcularDiasConsecutivosStatic("2026-07-04", 3, "2026-07-05"))
    }

    @Test
    fun `streak reinicia quando faltou um dia`() {
        assertEquals(1, ProgressRepository.calcularDiasConsecutivosStatic("2026-07-03", 5, "2026-07-05"))
    }
}
