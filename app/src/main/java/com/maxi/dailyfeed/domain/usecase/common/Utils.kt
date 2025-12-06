package com.maxi.dailyfeed.domain.usecase.common

object Utils {

    fun normalizeInputs(language: String, country: String): Pair<String, String> {
        val languageCode = language.ifBlank { "en" }
        val countryCode = country.ifBlank { "us" }

        return languageCode to countryCode
    }
}