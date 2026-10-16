package com.example.facturationapp

import android.content.Context
import java.io.*

class Panier {
    var art: ArrayList<Article> = ArrayList()

    // Constructeur secondaire (optionnel ici car la liste est déjà initialisée)
    constructor()

    // Fonction pour sauvegarder la liste d'articles dans un fichier binaire
    fun save(context: Context) {
        try {
            val fileOutput = context.openFileOutput("data", Context.MODE_PRIVATE)
            val objectOutput = ObjectOutputStream(fileOutput)
            objectOutput.writeObject(art)
            objectOutput.close()
            fileOutput.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fonction pour charger la liste d'articles depuis le fichier binaire
    fun load(context: Context) {
        try {
            val fileInput = context.openFileInput("data")
            val objectInput = ObjectInputStream(fileInput)
            @Suppress("UNCHECKED_CAST")
            art = objectInput.readObject() as ArrayList<Article>
            objectInput.close()
            fileInput.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}