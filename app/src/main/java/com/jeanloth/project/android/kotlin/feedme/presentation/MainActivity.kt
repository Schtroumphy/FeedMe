package com.jeanloth.project.android.kotlin.feedme.presentation

import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.jeanloth.project.android.kotlin.feedme.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.presentation.AppRoute.Companion.fromVal
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.FeedMeTheme
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.AddClientPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.CrewItem
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.HomePage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.PageTemplate

enum class AppRoute(val route: String, val title: String? =null) {
    HOME("home"),
    ADD_CLIENT("add_client", "Création client");

    companion object{
        fun fromVal(route: String?) = values().firstOrNull { it.route.equals(route) } ?: HOME
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val navController = rememberNavController()
            val currentRoute = fromVal(navController.currentBackStackEntry?.destination?.route)

            FeedMeTheme {
                // A surface container using the 'background' color from the theme
                PageTemplate(
                    title  = currentRoute.title ?: "",
                    displayHeader = currentRoute.title != null,
                    navController = navController,
                    content = {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomePage() }
                        composable("add_client") { AddClientPage(applicationContext) }
                    }
                })
            }
        }
    }
}
