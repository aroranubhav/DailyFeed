package com.maxi.dailyfeed.common

data class DBSyncMetaData(
    val isCached: Boolean = true,
    val cacheStatus: CacheStatus = CacheStatus.SYNCED,
    val cacheError: String? = null,
    val notNotified: Boolean = false,
    val timeStamp: Long? = System.currentTimeMillis()
)
