package com.info85.inforfacil.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Preferences sincronizadas (síncronas) usadas para configurações que precisam
 * estar disponíveis antes do setContentView (tema, fonte, contraste).
 * Devem ser atualizadas sempre que o usuário salvar as Configuracoes no DataStore.
 */
class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    var tema: String
        get() = prefs.getString(KEY_TEMA, "azul") ?: "azul"
        set(value) = prefs.edit().putString(KEY_TEMA, value).apply()

    var modoEscuro: Boolean
        get() = prefs.getBoolean(KEY_MODO_ESCURO, false)
        set(value) = prefs.edit().putBoolean(KEY_MODO_ESCURO, value).apply()

    var altoContraste: Boolean
        get() = prefs.getBoolean(KEY_ALTO_CONTRASTE, false)
        set(value) = prefs.edit().putBoolean(KEY_ALTO_CONTRASTE, value).apply()

    var botoesGrandes: Boolean
        get() = prefs.getBoolean(KEY_BOTOES_GRANDES, true)
        set(value) = prefs.edit().putBoolean(KEY_BOTOES_GRANDES, value).apply()

    var tamanhoFonte: String
        get() = prefs.getString(KEY_TAMANHO_FONTE, "medio") ?: "medio"
        set(value) = prefs.edit().putString(KEY_TAMANHO_FONTE, value).apply()

    var somAtivado: Boolean
        get() = prefs.getBoolean(KEY_SOM, true)
        set(value) = prefs.edit().putBoolean(KEY_SOM, value).apply()

    var vibracaoAtivada: Boolean
        get() = prefs.getBoolean(KEY_VIBRACAO, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRACAO, value).apply()

    companion object {
        private const val PREFS_NAME = "inforfacil_app_prefs"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_TEMA = "tema"
        private const val KEY_MODO_ESCURO = "modo_escuro"
        private const val KEY_ALTO_CONTRASTE = "alto_contraste"
        private const val KEY_BOTOES_GRANDES = "botoes_grandes"
        private const val KEY_TAMANHO_FONTE = "tamanho_fonte"
        private const val KEY_SOM = "som_ativado"
        private const val KEY_VIBRACAO = "vibracao_ativada"
    }
}
