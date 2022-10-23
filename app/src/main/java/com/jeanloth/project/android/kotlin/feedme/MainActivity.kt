package com.jeanloth.project.android.kotlin.feedme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeanloth.project.android.kotlin.feedme.core.theme.FeedMeTheme
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AddButtonActionType
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.AddCommandPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.BasketList
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandListPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CreateBasketPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.basket.BasketVM
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client.AddClientPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client.ClientListPage
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client.ClientVM
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client.PageTemplate
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.ProductVM
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute.Companion.fromVal
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.presentation.HomePage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val clientVM : ClientVM by viewModels()
    private val productVM : ProductVM by viewModels()
    private val basketVM : BasketVM by viewModels()

    @OptIn(ExperimentalComposeUiApi::class)
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
            val dialogType = rememberSaveable { (mutableStateOf<AddButtonActionType?>(null)) }

            val products by productVM.products.collectAsState()
            val baskets by basketVM.baskets.collectAsState()

            val title = fromVal(navBackStackEntry?.destination?.route).title
            topBarState.value = fromVal(navBackStackEntry?.destination?.route).title != null
            bottomBarState.value = fromVal(navBackStackEntry?.destination?.route).displayFooter
            displayBackOrCloseState.value = fromVal(navBackStackEntry?.destination?.route).displayBackOrClose
            displayAddButtonState.value = fromVal(navBackStackEntry?.destination?.route).displayAddButton
            dialogType.value = fromVal(navBackStackEntry?.destination?.route).dialogType

            val keyboardController = LocalSoftwareKeyboardController.current

            val scope = rememberCoroutineScope()

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
                    onCloseOrBackClick = {
                        keyboardController?.hide()
                        when(navController.currentDestination?.route){
                            FooterRoute.ADD_BASKET.route -> navController.popBackStack(FooterRoute.BASKETS.route, false)
                            else -> navController.popBackStack(FooterRoute.HOME.route, false)
                        }
                     },
                    onDialogDismiss = {
                       keyboardController?.hide()
                    } ,
                    addButtonActionType = dialogType.value,
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
                        composable(FooterRoute.BASKETS.route){
                            BasketList(baskets)
                        }

                        composable(FooterRoute.ADD_BASKET.route) {
                            CreateBasketPage(
                                products = products,
                                onValidateBasket = { label, price, productQuantity ->
                                    scope.launch {
                                        if(basketVM.saveBasket(label, price, productQuantity)) {
                                            splitties.toast.toast(R.string.basket_added)
                                        } else {
                                            splitties.toast.toast(R.string.basket_no_added)
                                        }
                                        navController.navigate(FooterRoute.BASKETS.route)
                                    }
                                },
                                onAddProduct = { name, uri ->
                                    productVM.saveProduct(name, uri?.path)
                                }
                            )
                        }

                        // Not in footer
                        composable(FooterRoute.ADD_COMMAND.route) { AddCommandPage() }
                        composable(FooterRoute.ADD_CLIENT.route) { AddClientPage(
                            onValidateClick = {
                                Toast.makeText(this@MainActivity, "Clic sur valider", Toast.LENGTH_SHORT)
                            }
                        ) }
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
