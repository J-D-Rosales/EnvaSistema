package com.example.envasistema.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.amplifyframework.core.Amplify
import com.amplifyframework.auth.result.step.AuthSignInStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isConfirmingNewPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isConfirmingNewPassword) "Cambio de Contraseña" else "Bienvenido",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (!isConfirmingNewPassword) {
            // Standard login fields
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario / Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading
            )
        } else {
            // Mandatory new password field
            Text(
                text = "Se requiere una nueva contraseña para su primer inicio de sesión.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                
                if (!isConfirmingNewPassword) {
                    // Initial Sign In
                    Amplify.Auth.signIn(username, password,
                        { result ->
                            coroutineScope.launch(Dispatchers.Main) {
                                when (result.nextStep.signInStep) {
                                    AuthSignInStep.DONE -> {
                                        Log.i("AuthQuickstart", "Sign in succeeded")
                                        onLoginSuccess()
                                    }
                                    AuthSignInStep.CONFIRM_SIGN_IN_WITH_NEW_PASSWORD -> {
                                        Log.i("AuthQuickstart", "Sign in requires new password")
                                        isConfirmingNewPassword = true
                                        isLoading = false
                                    }
                                    else -> {
                                        Log.i("AuthQuickstart", "Sign in step: ${result.nextStep.signInStep}")
                                        errorMessage = "Inicio de sesión no completado: ${result.nextStep.signInStep}"
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        { error ->
                            coroutineScope.launch(Dispatchers.Main) {
                                Log.e("AuthQuickstart", "Sign in failed", error)
                                errorMessage = error.message ?: "Error desconocido"
                                isLoading = false
                            }
                        }
                    )
                } else {
                    // Confirm Sign In with New Password
                    Amplify.Auth.confirmSignIn(newPassword,
                        { result ->
                            coroutineScope.launch(Dispatchers.Main) {
                                if (result.nextStep.signInStep == AuthSignInStep.DONE) {
                                    Log.i("AuthQuickstart", "Confirm sign in succeeded")
                                    onLoginSuccess()
                                } else {
                                    Log.i("AuthQuickstart", "Confirm sign in not done: ${result.nextStep.signInStep}")
                                    errorMessage = "No se pudo completar el registro de la nueva contraseña"
                                    isLoading = false
                                }
                            }
                        },
                        { error ->
                            coroutineScope.launch(Dispatchers.Main) {
                                Log.e("AuthQuickstart", "Confirm sign in failed", error)
                                errorMessage = error.message ?: "Error al confirmar contraseña"
                                isLoading = false
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isLoading && (
                if (isConfirmingNewPassword) newPassword.isNotBlank() 
                else (username.isNotBlank() && password.isNotBlank())
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(if (isConfirmingNewPassword) "Confirmar y Entrar" else "Iniciar Sesión")
            }
        }
    }
}
