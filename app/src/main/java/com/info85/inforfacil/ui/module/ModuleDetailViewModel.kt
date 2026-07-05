package com.info85.inforfacil.ui.module

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import com.info85.inforfacil.data.local.ModuloProgress
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository

class ModuleDetailViewModel(
    application: Application,
    private val moduleId: String
) : AndroidViewModel(application) {

    private val repository = ProgressRepository(ProgressDataStore(application))

    val moduloProgress: LiveData<ModuloProgress> = repository.progressComModulosDefault.map { progress ->
        progress.modulos[moduleId] ?: ModuloProgress(moduloId = moduleId)
    }.asLiveData()

    class Factory(
        private val application: Application,
        private val moduleId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ModuleDetailViewModel(application, moduleId) as T
        }
    }
}
