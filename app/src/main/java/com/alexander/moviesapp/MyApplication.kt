package com.alexander.moviesapp

import android.app.Application
import com.alexander.data.remote.Network

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Network.init("https://api.themoviedb.org/3/", BuildConfig.DEBUG)
    }
}
