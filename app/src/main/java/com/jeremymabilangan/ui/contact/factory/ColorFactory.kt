package com.jeremymabilangan.ui.contact.factory


/**
 * Created by Ralph Gabrielle Orden on 6/21/21.
 */

class ColorFactory {

    fun createColor(colorName: String): Color {
        return when (colorName) {
            "Red" -> Red()
            "Green" -> Green()
            else -> Blue()
        }
    }

}