
# Keystroke Biometrics App – Frontend (Android Jetpack Compose)

Cette application Android permet de capturer la dynamique de frappe d’un utilisateur (temps d'appui et d'enchaînement des touches) pour l’enregistrement et l’authentification biométrique via un backend Python (FastAPI + HMM).

---

##  Fonctionnalités

- Enregistrement de la dynamique de frappe
- Ajout de plusieurs frappes pour améliorer l'entraînement
- Envoi des données au backend via Retrofit (API `/register`)
- Authentification d’un utilisateur avec `/authenticate`
- Affichage de messages (`Toast`) indiquant le score HMM et le résultat

---

##  Technologies utilisées

- Kotlin
- Jetpack Compose
- Retrofit 2
- Gson
- Coroutine
- Material Design

---

##  Utilisation

### Enregistrement

1. Saisis un **nom d’utilisateur**
2. Tape la phrase (ex. `I am the best engineer`)
3. Clique sur **“Ajouter la frappe”** (répète 5 à 10 fois)
4. Clique sur **“Enregistrer”**
5. Un Toast s’affiche avec : `Modèle HMM entraîné pour [nom]`

###  Authentification

1. Tape à nouveau la phrase
2. Clique sur **“Authentifier”**
3. Un Toast affiche :

   * ✅ `Authentifié (Score: -12.3)`
   * ❌ `Refusé (Score: -132.5)`

---

##  Débogage

* Si l'app affiche `communication not permitted` :

  * Ajoute `network_security_config.xml`
  * Vérifie que le backend est lancé avec `--host 0.0.0.0`
  * Vérifie que téléphone et PC sont sur le **même réseau local (Wi-Fi)**

* Pour voir les erreurs :

  * Ouvre **Logcat** dans Android Studio
  * Filtre par ton application

---

##  Backend nécessaire

Le backend doit être lancé via :

```bash
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

> Le projet backend est basé sur **FastAPI + hmmlearn** et contient deux routes :

* `POST /register`
* `POST /authenticate`

---

##  Structure du projet

```
├── data/
│   └── KeystrokeEntry.kt
├── model/
│   └── KeystrokeSequence.kt
├── network/
│   ├── ApiClient.kt
│   └── ApiService.kt
├── ui/
│   └── AuthScreen.kt
├── res/xml/
│   └── network_security_config.xml
└── AndroidManifest.xml
```

---

##  Auteur

Ce projet a été développé dans le cadre d’un système de reconnaissance biométrique comportementale par frappe au clavier.
