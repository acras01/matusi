package ua.od.acros.matusi.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            fragmentFactory()
            loadKoinModules(appModules)
        }
    }
}