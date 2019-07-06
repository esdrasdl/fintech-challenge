package br.com.esdrasdl.challenge

import android.app.Application
import com.orhanobut.hawk.Hawk

class FintechApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
    }

}