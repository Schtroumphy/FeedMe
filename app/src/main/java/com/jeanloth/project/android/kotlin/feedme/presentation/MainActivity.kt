package com.jeanloth.project.android.kotlin.feedme.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeanloth.project.android.kotlin.feedme.presentation.FooterRoute.Companion.fromVal
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.FeedMeTheme
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.*

enum class FooterRoute(val route: String, val title: String? =null, val icon: ImageVector? = null, val inFooter: Boolean = false, val actionButton: Boolean = false, val displayFooter : Boolean = true) {
    HOME("home", title = null, Icons.Filled.Home, true, displayFooter = true),
    COMMAND_LIST("command_list", title = "Commandes", Icons.Filled.List, true, displayFooter = true),
    ADD_COMMAND_BUTTON("add_command", title = "Création commande", Icons.Filled.Add, true, true, false),
    CLIENT("clients", title = "Clients", Icons.Filled.Person, true, displayFooter = true),
    PRODUCTS("products", title = "Création panier", Icons.Filled.ShoppingCart, true, displayFooter = true),

    // Not in footer
    ADD_CLIENT("add_client", title = "Création client", displayFooter = false),
    ADD_COMMAND("add_command", title = "Création commande", displayFooter = false);

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
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = rememberSaveable {
                mutableStateOf(fromVal(navBackStackEntry?.destination?.route))
            }
            val topBarState = rememberSaveable { (mutableStateOf(true)) }
            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

            val title = fromVal(navBackStackEntry?.destination?.route).title
            topBarState.value = fromVal(navBackStackEntry?.destination?.route).title != null
            bottomBarState.value = fromVal(navBackStackEntry?.destination?.route).displayFooter

            FeedMeTheme {
                // A surface container using the 'background' color from the theme
                PageTemplate(
                    title  = title,
                    navController = navController,
                    displayHeader = topBarState.value,
                    displayBottomNav = bottomBarState.value,
                    currentRoute = currentRoute.value,
                    onCloseOrBackClick = { navController.popBackStack(FooterRoute.HOME.route, false) },
                    content = { innerPadding ->
                    NavHost(navController = navController, startDestination = FooterRoute.HOME.route, modifier = Modifier.padding(innerPadding)) {
                        composable(FooterRoute.HOME.route) { HomePage(navController) }
                        composable(FooterRoute.COMMAND_LIST.route) { CommandListPage() }
                        composable(FooterRoute.ADD_COMMAND_BUTTON.route) { AddCommandPage() }
                        composable(FooterRoute.CLIENT.route) { AddClientPage(applicationContext) }
                        composable(FooterRoute.PRODUCTS.route) { BasketPage(navController) }

                        // Not in footer
                        composable(FooterRoute.ADD_COMMAND.route) { AddCommandPage() }
                        composable(FooterRoute.ADD_CLIENT.route) { AddClientPage(applicationContext) }
                    }
                })
            }
        }
    }
}
