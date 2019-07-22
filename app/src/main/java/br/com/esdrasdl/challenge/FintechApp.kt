package br.com.esdrasdl.challenge

import android.app.Application
import br.com.esdrasdl.challenge.di.androidModule
import br.com.esdrasdl.challenge.di.dataModule
import br.com.esdrasdl.challenge.di.domainModule
import br.com.esdrasdl.challenge.di.presentationModule
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.security.ProviderInstaller
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FintechApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    androidModule,
                    dataModule,
                    domainModule,
                    presentationModule
                )
            )
        }

        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (ignored: GooglePlayServicesNotAvailableException) {

        } catch (ignored: Exception) {

        }
    }
}
