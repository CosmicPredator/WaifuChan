package com.cosmic.waifuchan.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.cosmic.waifuchan.models.ImageCategory
import com.cosmic.waifuchan.viewModels.MainViewModel

@Composable
fun FilterDialog(
    isExpanded: Boolean = false,
    itemSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
    items: List<String>,
    isNsfw: Boolean,
    viewModel: MainViewModel
) {

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedIndex = if (isNsfw) {
        items.indexOf(viewModel.selectedNsfwCategory.replaceFirstChar(Char::titlecase))
    } else {
        items.indexOf(viewModel.selectedSfwCategory.replaceFirstChar(Char::titlecase))
    }

    Column {
        DropdownMenu(
            expanded = isExpanded, onDismissRequest = onDismissRequest,
            modifier = Modifier.height(200.dp),
            properties = PopupProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                clippingEnabled = false
            )
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        itemSelected(item)
                        selectedIndex = index
                        viewModel.isDropDownExpanded = false
                    },
                    leadingIcon = {
                        if (selectedIndex == index) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Check Mark"
                            )
                        }
                    }
                )
            }
        }
    }
}