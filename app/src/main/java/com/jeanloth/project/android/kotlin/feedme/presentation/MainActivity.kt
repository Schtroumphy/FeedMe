package com.jeanloth.project.android.kotlin.feedme.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeanloth.project.android.kotlin.feedme.domain.FooterRoute
import com.jeanloth.project.android.kotlin.feedme.domain.FooterRoute.Companion.fromVal
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.FeedMeTheme
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.AddCommandPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.CommandListPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.PageTemplate
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.client.AddClientPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.client.ClientListPage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.home.HomePage
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.products.BasketPage

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
            val displayBackOrCloseState = rememberSaveable { (mutableStateOf(false)) }

            val title = fromVal(navBackStackEntry?.destination?.route).title
            topBarState.value = fromVal(navBackStackEntry?.destination?.route).title != null
            bottomBarState.value = fromVal(navBackStackEntry?.destination?.route).displayFooter
            displayBackOrCloseState.value = fromVal(navBackStackEntry?.destination?.route).displayBackOrClose

            FeedMeTheme {
                PageTemplate(
                    title  = title,
                    navController = navController,
                    displayHeader = topBarState.value,
                    displayBottomNav = bottomBarState.value,
                    displayBackOrClose = displayBackOrCloseState.value ,
                    currentRoute = currentRoute.value,
                    onCloseOrBackClick = { navController.popBackStack(FooterRoute.HOME.route, false) },
                    content = { innerPadding ->
                    NavHost(navController = navController, startDestination = FooterRoute.HOME.route, modifier = Modifier.padding(innerPadding)) {
                        composable(FooterRoute.HOME.route) { HomePage(navController) }
                        composable(FooterRoute.COMMAND_LIST.route) { CommandListPage() }
                        composable(FooterRoute.ADD_COMMAND_BUTTON.route) { AddCommandPage() }
                        composable(FooterRoute.CLIENT.route) { ClientListPage() }
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
