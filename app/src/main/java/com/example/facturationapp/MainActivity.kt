package com.example.facturationapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editLibelle: EditText
    private lateinit var editPrixHT: EditText
    private lateinit var spinnerTVA: Spinner
    private lateinit var textPrixTTC: TextView
    private lateinit var textTotalTTC: TextView
    private lateinit var tableArticles: TableLayout
    private lateinit var panier: Panier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des vues
        editLibelle = findViewById(R.id.editLibelle)
        editPrixHT = findViewById(R.id.editPrixHT)
        spinnerTVA = findViewById(R.id.spinnerTVA)
        textPrixTTC = findViewById(R.id.textPrixTTC)
        textTotalTTC = findViewById(R.id.textTotalTTC)
        tableArticles = findViewById(R.id.tableArticles)

        // Initialisation du panier
        panier = Panier()
        panier.load(this)

        // TVA Spinner
        val tvaValues = arrayOf(5, 10, 15, 20)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tvaValues)
        spinnerTVA.adapter = adapter

        // Affichage des anciens articles si existants
        for (article in panier.art) {
            addArticleToTable(article)
        }
        updateTotalTTC()

        // Bouton Ajouter
        findViewById<Button>(R.id.btnAjouter).setOnClickListener {
            ajouterArticle()
        }

        // Bouton Enregistrer
        findViewById<Button>(R.id.btnEnregistrer).setOnClickListener {
            panier.save(this)
            Toast.makeText(this, "Panier enregistré !", Toast.LENGTH_SHORT).show()
        }

        // Bouton Annuler
        findViewById<Button>(R.id.btnAnnuler).setOnClickListener {
            editLibelle.text.clear()
            editPrixHT.text.clear()
            textPrixTTC.text = "Prix TTC : "
        }
    }

    private fun ajouterArticle() {
        val libelle = editLibelle.text.toString()
        val prixHTStr = editPrixHT.text.toString()

        if (libelle.isBlank() || prixHTStr.isBlank()) {
            Toast.makeText(this, "Champs vides !", Toast.LENGTH_SHORT).show()
            return
        }

        val prixHT = prixHTStr.toDoubleOrNull()
        if (prixHT == null) {
            Toast.makeText(this, "Prix HT invalide !", Toast.LENGTH_SHORT).show()
            return
        }

        val tva = spinnerTVA.selectedItem.toString().toDouble()
        val prixTTC = prixHT + (prixHT * tva / 100)

        textPrixTTC.text = "Prix TTC : %.2f".format(prixTTC)

        val article = Article(libelle, prixHT)
        panier.art.add(article)
        addArticleToTable(article)
        updateTotalTTC()

        editLibelle.text.clear()
        editPrixHT.text.clear()
    }

    private fun addArticleToTable(article: Article) {
        val row = TableRow(this)

        val tva = spinnerTVA.selectedItem.toString().toDouble()
        val prixTTC = article.prixHT + (article.prixHT * tva / 100)

        row.addView(createTextView(article.libelle))
        row.addView(createTextView("%.2f".format(article.prixHT)))
        row.addView(createTextView("%.2f".format(prixTTC)))

        tableArticles.addView(row)
    }

    private fun createTextView(text: String): TextView {
        return TextView(this).apply {
            setPadding(8, 8, 8, 8)
            this.text = text
        }
    }

    private fun updateTotalTTC() {
        val tva = spinnerTVA.selectedItem.toString().toDouble()
        var total = 0.0
        for (article in panier.art) {
            total += article.prixHT + (article.prixHT * tva / 100)
        }
        textTotalTTC.text = "Total TTC : %.2f".format(total)
    }
}