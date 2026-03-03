package com.imirly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imirly.app.ui.auth.LoginScreen
import com.imirly.app.ui.auth.OnboardingScreen
import com.imirly.app.ui.auth.RegisterScreen
import com.imirly.app.ui.main.MainScreen
import com.imirly.app.ui.profile.MisAnunciosScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "onboarding") {
                composable("onboarding") { OnboardingScreen(navController) }
                composable("login") { LoginScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("main") { MainScreen(navController) }
                composable("mis_anuncios") { MisAnunciosScreen(navController) }
                composable("change_password") { com.imirly.app.ui.profile.ChangePasswordScreen(navController) }
                composable("saldo_screen") { com.imirly.app.ui.profile.SaldoScreen(navController) }
                composable("mirly_chat") { com.imirly.app.ui.profile.MirlyChatScreen(navController) }
                composable("about_mirly") { com.imirly.app.ui.profile.AboutMirlyScreen(navController) }
                composable("help_center") { com.imirly.app.ui.profile.HelpCenterScreen(navController) }
                composable("contact_us") { com.imirly.app.ui.profile.ContactUsScreen(navController) }
            }
        }
    }
}