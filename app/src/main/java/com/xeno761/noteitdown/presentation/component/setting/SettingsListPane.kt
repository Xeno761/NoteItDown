package com.xeno761.noteitdown.presentation.component.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.SdStorage
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.xeno761.noteitdown.R
import com.xeno761.noteitdown.presentation.component.TopBarTitle
import com.xeno761.noteitdown.presentation.glance.NoteListWidgetReceiver
import com.xeno761.noteitdown.presentation.screen.SettingsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListPane(
    navigateUp: () -> Unit,
    navigateToDetail: (SettingsItem) -> Unit
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                title = {
                    TopBarTitle(title = stringResource(id = R.string.settings))
                },
                actions = {
                    val scope = rememberCoroutineScope()
                    IconButton(onClick = {
                        scope.launch {
                            GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
                                NoteListWidgetReceiver::class.java
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Widgets,
                            contentDescription = "Widgets"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors()
                    .copy(scrolledContainerColor = TopAppBarDefaults.largeTopAppBarColors().containerColor),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            ListItem(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {
                        navigateToDetail(SettingsItem(0, R.string.style))
                    },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Palette,
                        contentDescription = "Style"
                    )
                },
                headlineContent = {
                    Text(
                        text = stringResource(R.string.style)
                    )
                },
                supportingContent = {
                    Text(
                        text = stringResource(R.string.dark_mode) + "  •  " + stringResource(R.string.color),
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
            )

            ListItem(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {
                        navigateToDetail(SettingsItem(1, R.string.data_security))
                    },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.SdStorage,
                        contentDescription = "Storage"
                    )
                },
                headlineContent = { Text(text = stringResource(R.string.data_security)) },
                supportingContent = {
                    Text(
                        text = stringResource(R.string.backup)
                                + "  •  " + stringResource(id = R.string.recovery)
                                + "  •  " + stringResource(id = R.string.password),
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
            )

        }
    }
}
