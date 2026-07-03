package com.info85.inforfacil.data.local

data class ModuloProgress(
    val moduloId: String,
    val nivel: Int = 1,
    val estrelas: Int = 0,
    val totalEstrelas: Int = 3,
    val concluido: Boolean = false,
    val percentualConcluido: Float = 0f
)

data class Configuracoes(
    val nomeUsuario: String = "Estudante",
    val somAtivado: Boolean = true,
    val vibracaoAtivada: Boolean = true,
    val tamanhoFonte: String = "normal"
)

data class ProgressModel(
    val modulos: Map<String, ModuloProgress> = emptyMap(),
    val totalEstrelas: Int = 0,
    val percentualGeral: Float = 0f,
    val configuracoes: Configuracoes = Configuracoes(),
    val versao: Int = 1
)
