package com.xeno761.noteitdown

import android.app.Activity
import android.os.Bundle
import com.xeno761.noteitdown.presentation.navigation.Screen
import com.xeno761.noteitdown.presentation.util.Constants.LINK
import com.xeno761.noteitdown.presentation.util.sendPendingIntent

class ManageSpaceActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.sendPendingIntent("$LINK/${Screen.Settings.route}")
        finish()
    }
}
