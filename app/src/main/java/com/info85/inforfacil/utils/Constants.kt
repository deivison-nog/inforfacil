package com.info85.inforfacil.utils

import com.info85.inforfacil.R
import com.info85.inforfacil.models.ModuleItem

object Constants {

    const val DATASTORE_NAME = "inforfacil_progress"

    val MODULE_IDS = listOf(
        "hardware",
        "sistema",
        "arquivos",
        "teclado",
        "internet",
        "seguranca",
        "dispositivos_moveis",
        "armazenamento_nuvem",
        "impressao_scanner",
        "multimidia",
        "manutencao_basica",
        "email"
    )

    fun getModulosDefault(): List<ModuleItem> = listOf(
        ModuleItem(
            id = "hardware",
            nome = "Hardware",
            descricao = "Componentes físicos do computador",
            iconResId = R.drawable.ic_hardware
        ),
        ModuleItem(
            id = "sistema",
            nome = "Sistema Operacional",
            descricao = "Windows, Linux e macOS",
            iconResId = R.drawable.ic_sistema
        ),
        ModuleItem(
            id = "arquivos",
            nome = "Arquivos e Pastas",
            descricao = "Organização e gerenciamento",
            iconResId = R.drawable.ic_arquivos
        ),
        ModuleItem(
            id = "teclado",
            nome = "Teclado e Mouse",
            descricao = "Atalhos e técnicas de digitação",
            iconResId = R.drawable.ic_teclado
        ),
        ModuleItem(
            id = "internet",
            nome = "Internet",
            descricao = "Navegação e segurança online",
            iconResId = R.drawable.ic_internet
        ),
        ModuleItem(
            id = "seguranca",
            nome = "Segurança Digital",
            descricao = "Proteção de dados e privacidade",
            iconResId = R.drawable.ic_seguranca
        ),
        ModuleItem(
            id = "dispositivos_moveis",
            nome = "Dispositivos Móveis",
            descricao = "Smartphones e tablets",
            iconResId = R.drawable.ic_dispositivos_moveis
        ),
        ModuleItem(
            id = "armazenamento_nuvem",
            nome = "Armazenamento em Nuvem",
            descricao = "Google Drive, OneDrive e mais",
            iconResId = R.drawable.ic_armazenamento_nuvem
        ),
        ModuleItem(
            id = "impressao_scanner",
            nome = "Impressão e Scanner",
            descricao = "Impressoras e digitalização",
            iconResId = R.drawable.ic_impressao_scanner
        ),
        ModuleItem(
            id = "multimidia",
            nome = "Multimídia",
            descricao = "Áudio, vídeo e imagens",
            iconResId = R.drawable.ic_multimidia
        ),
        ModuleItem(
            id = "manutencao_basica",
            nome = "Manutenção Básica",
            descricao = "Cuidados e dicas de manutenção",
            iconResId = R.drawable.ic_manutencao_basica
        ),
        ModuleItem(
            id = "email",
            nome = "E-mail",
            descricao = "Como usar e-mail com segurança",
            iconResId = R.drawable.ic_email
        )
    )
}
