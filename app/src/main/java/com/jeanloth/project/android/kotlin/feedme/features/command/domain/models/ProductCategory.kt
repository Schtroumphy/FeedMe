package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

enum class ProductCategory( val code : Int, val label : String) {
    FRUIT (1, "Fruit"),
    LEGUME(2, "Légume"),
    OTHER(3, "Autre");


    companion object{
        fun getProductCategoryFromCode(code : Int?) : ProductCategory = when(code) {
            FRUIT.code -> FRUIT
            LEGUME.code -> LEGUME
            else -> OTHER
        }

        fun getProductCategoryFromLabel(label : String?) : ProductCategory = when(label) {
            FRUIT.label -> FRUIT
            LEGUME.label -> LEGUME
            else -> OTHER
        }
    }
}