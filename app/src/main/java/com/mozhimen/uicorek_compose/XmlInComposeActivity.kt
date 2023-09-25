package com.mozhimen.uicorek_compose

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.w3c.dom.Text

/**
 * @ClassName XmlInComposeActivity
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/9/24 1:01
 * @Version 1.0
 */
class XmlInComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeInitXml()
        }
    }

    @Preview
    @Composable
    fun ComposeInitXml() {
        LazyColumn(modifier = Modifier.fillMaxWidth(), content = {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = Color.Green),
                    textAlign = TextAlign.Center,
                    text = "Compose header",
                )
            }
            items(count = 6) { index ->
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    factory = { context ->
                        (LayoutInflater.from(context).inflate(R.layout.item_xml_in_compose, null) as TextView).apply { text = "Xml Item $index" }
                    })
            }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(8.dp)
                        .background(color = Color.Yellow),
                    textAlign = TextAlign.Center,
                    text = "Compose Footer",
                )
            }
        })
    }
}