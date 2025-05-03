package com.example.keystrokebiometricsapp.ui


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.withContext


import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.keystrokebiometricsapp.model.*
import com.example.keystrokebiometricsapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen() {

    val allSequences = remember { mutableStateListOf<List<List<Long>>>() }
    val keystrokes = remember { mutableStateListOf<KeystrokeEntry>() }
    val context = LocalContext.current



    val username = remember { mutableStateOf("") }
    val inputText = remember { mutableStateOf(TextFieldValue("")) }

    var lastReleaseTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val keyboardController = LocalSoftwareKeyboardController.current





    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Nom d'utilisateur")
        TextField(value = username.value, onValueChange = { username.value = it })

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tapez la phrase d'exemple")
        TextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )


        // Capture les frappes à chaque lettre entrée
        LaunchedEffect(inputText.value) {
            val text = inputText.value.text
            if (text.isNotEmpty()) {
                val currentTime = System.currentTimeMillis()
                val flightTime = currentTime - lastReleaseTime
                val char = text.last()
                keystrokes.add(
                    KeystrokeEntry(
                        char = char,
                        pressTime = currentTime,
                        releaseTime = currentTime + 80, // simulate (à affiner avec KeyEvent plus tard)
                        flightTime = flightTime
                    )
                )
                lastReleaseTime = currentTime + 80
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val processed = keystrokes.map {
                    listOf(it.releaseTime - it.pressTime, it.flightTime)
                }
                if (processed.isNotEmpty()) {
                    allSequences.add(processed)
                    keystrokes.clear()
                    inputText.value = TextFieldValue("")  // vide le champ de saisie
                    Toast.makeText(context, "Frappe ajoutée (${allSequences.size})", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter la frappe")
        }

        Text("Frappe(s) enregistrée(s) : ${allSequences.size}/5")



        Row {
            Button(onClick = {
                val data = KeystrokeSequence(username.value, allSequences.flatten())

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiClient.apiService.register(data)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
            {
                Text("Enregistrer")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                val processed = keystrokes.map {
                    listOf(it.releaseTime - it.pressTime, it.flightTime)
                }

                val data = KeystrokeSequence(username.value, processed)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val result = ApiClient.apiService.authenticate(data)

                        withContext(Dispatchers.Main) {
                            val message = if (result.authenticated) {
                                "✅ Authentifié avec succès ! Score: ${result.score}"
                            } else {
                                "❌ Échec de l'authentification. Score: ${result.score}"
                            }

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erreur réseau : ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }) {
                Text("Authentifier")
            }

        }
    }
}
