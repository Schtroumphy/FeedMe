package com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AddButtonActionType

enum class FooterRoute(val route: String, val title: String? =null, val icon: ImageVector? = null, val inFooter: Boolean = false, val actionButton: Boolean = false, val displayFooter : Boolean = true, val displayBackOrClose : Boolean = false, val displayAddButton : Boolean = false, val dialogType: AddButtonActionType? = null) {
    HOME("home", icon = Icons.Filled.Home, inFooter = true, displayFooter = true),
    COMMAND_LIST("command_list", title = "Commandes", icon = Icons.Filled.List, inFooter = true, displayFooter = true),
    ADD_COMMAND_BUTTON("add_command", title = "Création commande", Icons.Filled.Add, true, true, false, displayBackOrClose = true),
    CLIENT("clients", title = "Clients", Icons.Filled.Person, true, displayFooter = true, dialogType = AddButtonActionType.ADD_CLIENT, displayAddButton = true),
    BASKETS("baskets", title = "Paniers", Icons.Filled.ShoppingCart, true, displayFooter = true, dialogType = AddButtonActionType.ADD_BASKET, displayBackOrClose = false, displayAddButton = true),

    // Not in footer
    PRODUCTS("products", title = "Produits", Icons.Filled.ShoppingCart, false, displayFooter = true, displayBackOrClose = false, displayAddButton = true),
    ADD_CLIENT("add_client", title = "Création client", displayFooter = false, displayBackOrClose = true),
    ADD_COMMAND("add_command", title = "Création commande", displayFooter = false, displayBackOrClose = true),
    ADD_PRODUCTS("add_products", title = "Ajouter un produit", Icons.Filled.ShoppingCart, false, displayFooter = false, displayBackOrClose = true),
    ADD_BASKET("add_basket", title = "Créer un panier", Icons.Filled.ShoppingCart, false, displayFooter = false, displayBackOrClose = true),

    COMMAND_DETAIL("command_detail/{commandId}", title = null, inFooter = false, displayFooter = false, displayBackOrClose = true);

    companion object{
        fun fromVal(route: String?) = values()
            .firstOrNull { it.route == route } ?: HOME
    }
}