package com.info85.inforfacil.models

data class ModuleItem(
    val id: String,
    val nome: String,
    val descricao: String,
    val iconResId: Int,
    val nivel: Int = 1,
    val estrelas: Int = 0,
    val totalEstrelas: Int = 3,
    val concluido: Boolean = false,
    val percentualConcluido: Float = 0f
)
