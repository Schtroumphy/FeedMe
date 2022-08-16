package com.jeanloth.project.android.kotlin.feedme.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeanloth.project.android.kotlin.feedme.presentation.FooterRoute.Companion.fromVal
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.FeedMeTheme
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.AddClientPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.BasketPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.HomePage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.PageTemplate

enum class FooterRoute(val route: String, val title: String? =null, val icon: ImageVector? = null, val inFooter: Boolean = false, val actionButton: Boolean = false, val displayFooter : Boolean = true) {
    HOME("home", null, Icons.Filled.Home, true, displayFooter = true),
    COMMAND_LIST("add_command", null, Icons.Filled.List, true, displayFooter = true),
    ADD_COMMAND_BUTTON("add_command", "Création commande", Icons.Filled.Add, true, true),
    CLIENT("add_client", "Client", Icons.Filled.Person, true, displayFooter = false),
    PRODUCTS("products", "Produits", Icons.Filled.ShoppingCart, true, displayFooter = false),
    ADD_CLIENT("add_client", "Création client", displayFooter = false),
    ADD_COMMAND("add_command", "Création commande", displayFooter = false);

    companion object{
        fun fromVal(route: String?) = values()
            .firstOrNull { it.route == route } ?: HOME
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
                    displayBottomNav = false,
                    content = { innerPadding ->
                    NavHost(navController = navController, startDestination = FooterRoute.PRODUCTS.route, modifier = Modifier.padding(innerPadding)) {
                        composable(FooterRoute.HOME.route) { HomePage(navController) }
                        composable(FooterRoute.COMMAND_LIST.route) { HomePage(navController) }
                        composable(FooterRoute.CLIENT.route) { AddClientPage(applicationContext) }
                        composable(FooterRoute.PRODUCTS.route) { BasketPage(navController) }
                        composable(FooterRoute.ADD_COMMAND.route) { BasketPage() }
                        composable(FooterRoute.ADD_CLIENT.route) { AddClientPage(applicationContext) }
                    }
                })
            }
        }
    }
}
