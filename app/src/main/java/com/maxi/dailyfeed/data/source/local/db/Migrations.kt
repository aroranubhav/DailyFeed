package com.maxi.dailyfeed.data.source.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS news_worker_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    timeStamp INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    message TEXT
                )
            """.trimIndent()
        )
    }
}