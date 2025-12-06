package com.maxi.dailyfeed.data.source.common

object DataConstants {

    object EndPoints {
        const val TOP_HEADLINES = "top-headlines"
    }

    object Keys {

        const val STATUS = "status"
        const val TOTAL_RESULTS = "totalResults"
        const val ARTICLES = "articles"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val URL = "url"
        const val IMAGE_URL = "urlToImage"
        const val PUBLISHED_AT = "publishedAt"
    }

    object QueryParams {

        const val LANGUAGE = "language"
        const val COUNTRY = "country"
    }

    object Headers {

        const val API_KEY = "x-api-key"
        const val USER_AGENT = "User-Agent"
        const val X_FORCE_REFRESH = "X-Force-Refresh"
    }

    object Tables {

        const val ARTICLES = "articles"
    }
}