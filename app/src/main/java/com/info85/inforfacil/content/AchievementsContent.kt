package com.info85.inforfacil.content

import com.info85.inforfacil.R

data class AchievementDefinition(
    val id: String,
    val name: String,
    val description: String,
    val iconResId: Int
)

object AchievementsContent {
    fun definitions(): List<AchievementDefinition> = listOf(
        a("primeiro_passo", "Primeiro Passo", "Concluir o primeiro módulo", R.drawable.ic_hardware),
        a("aprendiz_tecnologia", "Aprendiz de Tecnologia", "Concluir 3 módulos", R.drawable.ic_sistema),
        a("explorador_digital", "Explorador Digital", "Concluir 6 módulos", R.drawable.ic_internet),
        a("mestre_informatica", "Mestre da Informática", "Concluir os 12 módulos", R.drawable.ic_manutencao_basica),
        a("estrela_brilhante", "Estrela Brilhante", "Conseguir 3 estrelas em 5 módulos", R.drawable.ic_multimidia),
        a("persistente", "Persistente", "Estudar por 7 dias consecutivos", R.drawable.ic_teclado),
        a("colecionador", "Colecionador", "Obter 20 estrelas no total", R.drawable.ic_arquivos),
        a("especialista_email", "Especialista em E-mail", "Concluir E-mail com 3 estrelas", R.drawable.ic_email),
        a("navegador_seguro", "Navegador Seguro", "Concluir Segurança com 3 estrelas", R.drawable.ic_seguranca),
        a("mestre_teclado", "Mestre do Teclado", "Concluir Teclado com 3 estrelas", R.drawable.ic_teclado)
    )

    private fun a(id: String, name: String, description: String, icon: Int) =
        AchievementDefinition(id, name, description, icon)
}
