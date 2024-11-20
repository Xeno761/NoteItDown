package com.xeno761.noteitdown.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xeno761.noteitdown.MainActivity
import com.xeno761.noteitdown.presentation.viewmodel.SharedViewModel
import com.xeno761.noteitdown.presentation.component.MaskAnimModel
import com.xeno761.noteitdown.presentation.component.MaskBox
import com.xeno761.noteitdown.presentation.navigation.AnimatedNavHost
import com.xeno761.noteitdown.presentation.state.AppTheme
import com.xeno761.noteitdown.presentation.state.AppTheme.Companion.toInt
import com.xeno761.noteitdown.presentation.theme.NoteitdownTheme
import com.xeno761.noteitdown.presentation.util.Constants

@Composable
fun BaseScreen(
    sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as MainActivity)
) {

    val settingsState by sharedViewModel.settingsStateFlow.collectAsStateWithLifecycle()

    // Biometric authentication
    var authenticated by rememberSaveable {
        mutableStateOf(false)
    }

    // Check if the user is logged in
    val loggedIn by remember {
        derivedStateOf {
            !settingsState.needPassword || authenticated
        }
    }

    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(settingsState.theme) {
        if (settingsState.theme == AppTheme.SYSTEM) {
            sharedViewModel.putPreferenceValue(
                Constants.Preferences.IS_APP_IN_DARK_MODE,
                isSystemInDarkTheme
            )
        }
    }

    NoteitdownTheme(
        color = settingsState.color,
        darkMode = settingsState.isAppInDarkMode
    ) {

        // MaskBox is a custom composable that animates a mask over the screen
        MaskBox(
            maskComplete = {
                sharedViewModel.putPreferenceValue(
                    Constants.Preferences.IS_APP_IN_DARK_MODE,
                    !settingsState.isAppInDarkMode
                )
            },
            animFinish = {
                sharedViewModel.putPreferenceValue(Constants.Preferences.IS_SWITCH_ACTIVE, false)
                if (settingsState.shouldFollowSystem) {
                    sharedViewModel.putPreferenceValue(
                        Constants.Preferences.APP_THEME,
                        AppTheme.SYSTEM.toInt()
                    )
                }
            }
        ) { maskActiveEvent ->

            LaunchedEffect(settingsState.isSwitchActive) {
                if (!settingsState.isSwitchActive) return@LaunchedEffect

                if (settingsState.isAppInDarkMode)
                    maskActiveEvent(
                        MaskAnimModel.SHRINK,
                        Constants.Preferences.MASK_CLICK_X,
                        Constants.Preferences.MASK_CLICK_Y
                    )
                else
                    maskActiveEvent(
                        MaskAnimModel.EXPEND,
                        Constants.Preferences.MASK_CLICK_X,
                        Constants.Preferences.MASK_CLICK_Y
                    )
            }

            val blur by animateDpAsState(targetValue = if (!loggedIn) 16.dp else 0.dp, label = "Blur")

            val windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            val isLargeScreen by remember {
                derivedStateOf {
                    windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
                            && windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT
                }
            }

            AnimatedNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .blur(blur),
                isLargeScreen = isLargeScreen
            )

            AnimatedVisibility(visible = !loggedIn, enter = fadeIn(), exit = fadeOut()) {
                LoginOverlayScreen(
                    onAuthenticated = {
                        authenticated = true
                    },
                    onAuthenticationNotEnrolled = {
                        sharedViewModel.putPreferenceValue(Constants.Preferences.NEED_PASSWORD, false)
                    }
                )
            }
        }
    }
}
