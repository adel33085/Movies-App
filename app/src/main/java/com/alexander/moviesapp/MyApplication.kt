package com.alexander.moviesapp

import android.app.Application
import com.alexander.data.remote.Network
import com.alexander.moviesapp.di.getModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Network.init("https://api.themoviedb.org/3/", BuildConfig.DEBUG)
        startKoin {
            androidContext(this@MyApplication)
            modules(getModules().toList())
        }
    }
}
