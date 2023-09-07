package ua.od.acros.matusi.presentation.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import ua.od.acros.matusi.presentation.authorization.navigation.authNavGraph

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        setupKoinFragmentFactory()

        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "auth") {
                authNavGraph(navController = navController)
            }
        }
    }
}