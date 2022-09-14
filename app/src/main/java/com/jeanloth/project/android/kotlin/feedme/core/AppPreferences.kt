package com.jeanloth.project.android.kotlin.feedme.core

import splitties.preferences.Preferences

object AppPreferences : Preferences("feed_me_preferences"){

    var databaseVersion by intPref("databaseVersion", defaultValue = -1)
}