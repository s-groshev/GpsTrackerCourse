package com.nosta.gpstrackercourse

import android.app.Application
import com.nosta.gpstrackercourse.db.MainDb

class MainApp : Application() {
    val database by lazy { MainDb.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
    }
}