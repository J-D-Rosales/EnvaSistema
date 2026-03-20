package com.example.envasistema.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.envasistema.data.repository.OperationRepository
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.injectAuthData
import kotlinx.coroutines.launch

class OperationViewModel(private val repository: OperationRepository) : ViewModel() {

    /**
     * Saves the operation payload to the local Room database via the repository.
     * Before saving, it injects the current authenticated user's email and name.
     */
    fun saveOperation(payload: OperationPayload) {
        viewModelScope.launch {
            val enrichedPayload = payload.injectAuthData()
            repository.saveOperation(enrichedPayload)
        }
    }
}

class OperationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OperationViewModel(OperationRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
