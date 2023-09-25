package com.mozhimen.uicorek_compose

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mozhimen.basick.elemk.androidx.appcompat.bases.BaseActivityVB
import com.mozhimen.uicorek_compose.databinding.ActivityComposeInXmlBinding

class ComposeInXmlActivity : BaseActivityVB<ActivityComposeInXmlBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.inXmlRoot.setContent {
            ComposeListText()
        }
    }

    @Preview
    @Composable
    fun ComposeListText() {
        LazyColumn(modifier = Modifier.fillMaxWidth(), content = {
            items(count = 10) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), text = "Compose List Item $it"
                )
            }
        })
    }
}