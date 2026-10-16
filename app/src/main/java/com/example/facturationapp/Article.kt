package com.example.facturationapp

import java.io.Serializable

data class Article(
    val libelle: String,
    val prixHT: Double
) : Serializable