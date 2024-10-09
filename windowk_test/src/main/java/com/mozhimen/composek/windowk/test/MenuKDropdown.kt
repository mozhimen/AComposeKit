package com.mozhimen.composek.windowk.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.mozhimen.kotlin.elemk.commons.IA_Listener

@Preview(widthDp = 200)
@Composable
fun PreviewMenuKDropDown() {
    MenuKDropdown {

    }
}

@Composable
fun MenuKDropdown(
    textStyle: TextStyle = TextStyle.Default,
    textModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier, //
    menuItems: List<String> = listOf("a", "b"), // Menu Options
    menuItemDefaultSelectedIndex: Int = 0, // Default Selected Option on load
    menuItemModifier: Modifier = Modifier,
    menuModifier: Modifier = Modifier,
    menuOnSelected: IA_Listener<Int>, // Pass the Selected Option
) {
    var reMenuSelectIndex by remember { mutableStateOf(0) }
    var reMenuExpand by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = boxModifier
            .clickable {
                reMenuExpand = true
            }
    ) {
        Text(
            text = menuItems.getOrNull(menuItemDefaultSelectedIndex) ?: "?",
            color = textStyle.color,
            fontSize = textStyle.fontSize,
            fontWeight = textStyle.fontWeight,
            modifier = textModifier
        )
        DropdownMenu(
            expanded = reMenuExpand,
            onDismissRequest = {
                reMenuExpand = false
            },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            modifier = menuModifier
        ) {
            menuItems.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        reMenuSelectIndex = index
                        reMenuExpand = false
                        menuOnSelected(reMenuSelectIndex)
                    }
                ) {
                    Text(
                        text = item,
                        color = textStyle.color,
                        textAlign = textStyle.textAlign,
                        modifier = menuItemModifier
                    )
                }
            }
        }
    }
}