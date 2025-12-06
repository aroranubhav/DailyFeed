package com.maxi.dailyfeed.domain.repository

interface AppDataStore {

    suspend fun getIsAlreadyLaunched(): Boolean

    suspend fun updateIsAlreadyLaunched()
}