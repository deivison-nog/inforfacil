package com.info85.inforfacil.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.info85.inforfacil.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "inforfacil_progress")

class ProgressDataStore(private val context: Context) {

    private val gson = Gson()

    private object Keys {
        val PROGRESS_JSON = stringPreferencesKey("progress_json")
    }

    val progressFlow: Flow<ProgressModel> = context.dataStore.data.map { preferences ->
        val json = preferences[Keys.PROGRESS_JSON]
        if (json.isNullOrEmpty()) {
            ProgressModel()
        } else {
            try {
                val type = object : TypeToken<ProgressModel>() {}.type
                gson.fromJson(json, type) ?: ProgressModel()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao desserializar progresso do DataStore", e)
                ProgressModel()
            }
        }
    }

    suspend fun saveProgress(progress: ProgressModel) {
        val json = gson.toJson(progress)
        context.dataStore.edit { preferences ->
            preferences[Keys.PROGRESS_JSON] = json
        }
    }

    suspend fun updateModuloProgress(moduloId: String, moduloProgress: ModuloProgress) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[Keys.PROGRESS_JSON]
            val currentProgress = if (currentJson.isNullOrEmpty()) {
                ProgressModel()
            } else {
                try {
                    val type = object : TypeToken<ProgressModel>() {}.type
                    gson.fromJson(currentJson, type) ?: ProgressModel()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao desserializar progresso ao atualizar módulo", e)
                    ProgressModel()
                }
            }
            val updatedModulos = currentProgress.modulos.toMutableMap()
            updatedModulos[moduloId] = moduloProgress
            val totalEstrelas = updatedModulos.values.sumOf { it.estrelas }
            val percentualGeral = if (updatedModulos.isNotEmpty()) {
                updatedModulos.values.map { it.percentualConcluido }.average().toFloat()
            } else 0f
            val updatedProgress = currentProgress.copy(
                modulos = updatedModulos,
                totalEstrelas = totalEstrelas,
                percentualGeral = percentualGeral
            )
            preferences[Keys.PROGRESS_JSON] = gson.toJson(updatedProgress)
        }
    }

    suspend fun updateConfiguracoes(configuracoes: Configuracoes) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[Keys.PROGRESS_JSON]
            val currentProgress = if (currentJson.isNullOrEmpty()) {
                ProgressModel()
            } else {
                try {
                    val type = object : TypeToken<ProgressModel>() {}.type
                    gson.fromJson(currentJson, type) ?: ProgressModel()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao desserializar progresso ao atualizar configurações", e)
                    ProgressModel()
                }
            }
            val updatedProgress = currentProgress.copy(configuracoes = configuracoes)
            preferences[Keys.PROGRESS_JSON] = gson.toJson(updatedProgress)
        }
    }

    suspend fun clearProgress() {
        context.dataStore.edit { preferences ->
            preferences.remove(Keys.PROGRESS_JSON)
        }
    }

    suspend fun exportProgressJson(): String {
        val preferences = context.dataStore.data.first()
        return preferences[Keys.PROGRESS_JSON] ?: gson.toJson(ProgressModel())
    }

    suspend fun importProgressJson(json: String): Boolean {
        return try {
            val type = object : TypeToken<ProgressModel>() {}.type
            val imported = gson.fromJson<ProgressModel>(json, type) ?: return false
            val sanitized = sanitizeProgress(imported)
            context.dataStore.edit { preferences ->
                preferences[Keys.PROGRESS_JSON] = gson.toJson(sanitized)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao importar progresso JSON", e)
            false
        }
    }

    private fun sanitizeProgress(progress: ProgressModel): ProgressModel {
        val sanitizedModules = Constants.MODULE_IDS.associateWith { moduleId ->
            val modulo = progress.modulos[moduleId] ?: ModuloProgress(moduloId = moduleId)
            modulo.copy(
                moduloId = moduleId,
                nivel = modulo.nivel.coerceAtLeast(1),
                estrelas = modulo.estrelas.coerceIn(0, 3),
                percentualConcluido = modulo.percentualConcluido.coerceIn(0f, 1f)
            )
        }

        val sanitizedFavoritos = progress.glossarioFavoritos.filter { it.isNotBlank() }.toSet()
        val sanitizedConquistas = progress.conquistasDesbloqueadas.filterKeys { it.isNotBlank() }
        val totalEstrelas = sanitizedModules.values.sumOf { it.estrelas }
        val percentualGeral = if (sanitizedModules.isEmpty()) 0f else sanitizedModules.values.map { it.percentualConcluido }.average().toFloat()

        return progress.copy(
            modulos = sanitizedModules,
            totalEstrelas = totalEstrelas,
            percentualGeral = percentualGeral,
            glossarioFavoritos = sanitizedFavoritos,
            conquistasDesbloqueadas = sanitizedConquistas,
            diasConsecutivos = progress.diasConsecutivos.coerceAtLeast(0)
        )
    }

    companion object {
        private const val TAG = "ProgressDataStore"
    }
}
