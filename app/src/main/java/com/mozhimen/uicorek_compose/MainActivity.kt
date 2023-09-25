package com.mozhimen.uicorek_compose

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mozhimen.basick.elemk.commons.I_Listener
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek_compose.ui.theme.UicoreKit_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UicoreKit_ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    content = {
                        ComposeListButton()
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewComposeListButton() {
    UicoreKit_ComposeTheme {
        ComposeListButton()
    }
}

@Composable
fun ComposeListButton() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        content = {
            ComposeButton("goComposeInXml") { context.startContext<ComposeInXmlActivity>() }
            ComposeButton("goXmlInCompose") { context.startContext<XmlInComposeActivity>() }
        }
    )
}

@Composable
fun ComposeButton(name: String, onClick: I_Listener) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        content = {
            Text(
                text = name
            )
        }
    )
}