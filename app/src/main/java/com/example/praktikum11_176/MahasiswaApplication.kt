package com.example.praktikum11_176

import android.app.Application
import com.example.praktikum11_176.di.AppContainer
import com.example.praktikum11_176.di.MahasiswaContainer


class MahasiswaApplications:Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer()
    }
}