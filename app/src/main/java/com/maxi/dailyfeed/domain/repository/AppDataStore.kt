package com.maxi.dailyfeed.domain.repository

interface AppDataStore {

    suspend fun getIsAlreadyLaunched(): Boolean

    suspend fun updateIsAlreadyLaunched()

    suspend fun getETag(): String?

    suspend fun saveETag(value: String)
}