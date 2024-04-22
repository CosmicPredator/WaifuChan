package com.cosmic.waifuchan.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Support
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.cosmic.waifuchan.models.ThemeMode


@Composable
fun SettingsPage(
    onThemeUpdated: (ThemeMode) -> Unit
) {
    var openDialog by rememberSaveable {
        mutableStateOf(false)
    }

    when (openDialog) {
        true -> ThemeChangeDialog(
            dismissRequest = {
                openDialog = false
            },
            onSelectionChanged = {themeMode ->
                onThemeUpdated(themeMode)
            }
        )
        else -> {  }
    }

    val uriHandler = LocalUriHandler.current

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column (
            modifier = Modifier.padding(top = 10.dp)
        ) {
            SettingsMenuLink(
                title = { Text(text = "Theme") },
                subtitle = { Text(text = "Set your preferred theme") },
                icon = {
                    Icon(imageVector = Icons.Rounded.FormatPaint, contentDescription = "ChangeTheme")
                },
                onClick = { openDialog = !openDialog }
            )
            SettingsMenuLink(
                title = { Text(text = "Github") },
                subtitle = { Text(text = "Support this project") },
                icon = {
                    Icon(imageVector = Icons.Rounded.Support, contentDescription = "Support")
                },
                onClick = {
                    uriHandler.openUri(
                        "https://github.com/CosmicPredator/WaifuChan"
                    )
                }
            )
            SettingsMenuLink(
                title = { Text(text = "App Version") },
                subtitle = { Text(text = "Version 1.0") },
                onClick = {  },
                icon = {
                    Icon(imageVector = Icons.Rounded.Code, contentDescription = "Version")
                }
            )
        }
    }
}


@Composable
fun ThemeChangeDialog(
    dismissRequest: () -> Unit,
    onSelectionChanged: (ThemeMode) -> Unit
) {

    var themeMode by rememberSaveable {
        mutableStateOf(ThemeMode.DEFAULT)
    }

    Dialog(
        onDismissRequest = dismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column (
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)
            ) {

                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(10.dp))

                RadioText(
                    isSelected = themeMode == ThemeMode.LIGHT,
                    selectionChanged = {
                        if (it) {
                            themeMode = ThemeMode.LIGHT
                            onSelectionChanged(themeMode)
                        }
                    },
                    content = "Light"
                )

                RadioText(
                    isSelected = themeMode == ThemeMode.DARK,
                    selectionChanged = {
                        if (it) {
                            themeMode = ThemeMode.DARK
                            onSelectionChanged(themeMode)
                        }
                    },
                    content = "Dark"
                )

                RadioText(
                    isSelected = themeMode == ThemeMode.DEFAULT,
                    selectionChanged = {
                        if (it) {
                            themeMode = ThemeMode.DEFAULT
                            onSelectionChanged(themeMode)
                        }
                    },
                    content = "System Default"
                )
                
                Spacer(modifier = Modifier.height(10.dp))

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, bottom = 2.dp),
                    horizontalArrangement = Arrangement.Absolute.Right) {
                    TextButton(onClick = dismissRequest) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun RadioText(
    isSelected: Boolean,
    selectionChanged: (Boolean) -> Unit,
    content: String
) {
    Row (
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                selectionChanged(!isSelected)
            }
            .fillMaxWidth()
    ) {
        RadioButton(selected = isSelected, onClick = {
            selectionChanged(!isSelected)
        })
        Text(text = content)
    }
}