package com.jeanloth.project.android.kotlin.feedme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.jeanloth.project.android.kotlin.feedme.core.theme.FeedMeTheme
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.DialogType
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client.AddClientPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.BasketPage
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute.Companion.fromVal
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.presentation.HomePage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandListPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client.ClientListPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client.ClientVM
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client.PageTemplate
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.ProductVM
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.AddCommandPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val clientVM : ClientVM by viewModels()
    private val productVM : ProductVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        productVM.syncProducts()

        setContent {

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = rememberSaveable {
                mutableStateOf(fromVal(navBackStackEntry?.destination?.route))
            }
            val topBarState = rememberSaveable { (mutableStateOf(true)) }
            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
            val displayBackOrCloseState = rememberSaveable { (mutableStateOf(false)) }
            val displayAddButtonState = rememberSaveable { (mutableStateOf(false)) }
            val dialogType = rememberSaveable { (mutableStateOf<DialogType?>(null)) }

            val title = fromVal(navBackStackEntry?.destination?.route).title
            topBarState.value = fromVal(navBackStackEntry?.destination?.route).title != null
            bottomBarState.value = fromVal(navBackStackEntry?.destination?.route).displayFooter
            displayBackOrCloseState.value = fromVal(navBackStackEntry?.destination?.route).displayBackOrClose
            displayAddButtonState.value = fromVal(navBackStackEntry?.destination?.route).displayAddButton
            dialogType.value = fromVal(navBackStackEntry?.destination?.route).dialogType

            FeedMeTheme {
                PageTemplate(
                    context = this,
                    title  = title,
                    navController = navController,
                    displayHeader = topBarState.value,
                    displayBottomNav = bottomBarState.value,
                    displayBackOrClose = displayBackOrCloseState.value ,
                    displayAddButton = displayAddButtonState.value ,
                    currentRoute = currentRoute.value,
                    onCloseOrBackClick = { navController.popBackStack(FooterRoute.HOME.route, false) },
                    addDialogType = dialogType.value,
                    content = { innerPadding ->
                    NavHost(navController = navController, startDestination = FooterRoute.HOME.route, modifier = Modifier.padding(innerPadding)) {
                        composable(FooterRoute.HOME.route) { HomePage(navController) }
                        composable(FooterRoute.COMMAND_LIST.route) { CommandListPage() }
                        composable(FooterRoute.ADD_COMMAND_BUTTON.route) { AddCommandPage() }
                        composable(FooterRoute.CLIENT.route) {
                            ClientListPage(clientVM,
                                onClientRemoved = {
                                    clientVM.removeClient(it)
                                }
                            )
                        }
                        composable(FooterRoute.PRODUCTS.route) { BasketPage(productVM, applicationContext) }

                        // Not in footer
                        composable(FooterRoute.ADD_COMMAND.route) { AddCommandPage() }
                        composable(FooterRoute.ADD_CLIENT.route) { AddClientPage(applicationContext) }
                        }
                    },
                    onNewClientAdded = {
                        clientVM.saveClient(it)
                    }
                )
            }
        }
    }
}
