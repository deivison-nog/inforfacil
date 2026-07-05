package com.info85.inforfacil.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.info85.inforfacil.R

object ThemeHelper {

    /**
     * Aplica o tema correto baseado nas preferências salvas.
     * Deve ser chamado ANTES de super.onCreate() em cada Activity.
     */
    fun applyTheme(context: Context, prefs: AppPreferences) {
        val themeResId = resolveThemeResId(prefs)
        (context as? android.app.Activity)?.setTheme(themeResId)
    }

    /**
     * Retorna o ID do tema Material correto conforme tema + altoContraste.
     */
    fun resolveThemeResId(prefs: AppPreferences): Int {
        if (prefs.altoContraste) return R.style.Theme_InfoFacil_AltoContraste
        return when (prefs.tema) {
            "verde"   -> R.style.Theme_InfoFacil_Verde
            "roxo"    -> R.style.Theme_InfoFacil_Roxo
            "laranja" -> R.style.Theme_InfoFacil_Laranja
            else      -> R.style.Theme_InfoFacil
        }
    }

    /**
     * Aplica modo escuro / claro globalmente usando AppCompatDelegate.
     */
    fun applyDarkMode(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    /**
     * Retorna o fontScale para ser aplicado em attachBaseContext.
     */
    fun fontScale(prefs: AppPreferences): Float {
        val base = when (prefs.tamanhoFonte) {
            "pequeno" -> 0.85f
            "grande"  -> 1.2f
            else      -> 1.0f
        }
        return if (prefs.botoesGrandes && base < 1.1f) base * 1.1f else base
    }

    /**
     * Cria um novo contexto com o fontScale correto.
     */
    fun createFontScaledContext(base: Context, prefs: AppPreferences): Context {
        val scale = fontScale(prefs)
        val config = Configuration(base.resources.configuration)
        config.fontScale = scale
        return base.createConfigurationContext(config)
    }
}
