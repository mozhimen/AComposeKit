package com.mozhimen.composek.imagek

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.glide.rememberGlidePainter

/**
 * @ClassName AsyncImageGlide
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/5
 * @Version 1.0
 */

@Composable
fun CenteredImage(
    model: String,
    modifier: Modifier = Modifier
) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier.fillMaxSize() // 确保Box填满父容器
//    ) {
        Image(
            painter = rememberGlidePainter(request = model),
            contentDescription = null,
            modifier = Modifier.size(200.dp) // 指定图像的大小，你可以根据需要调整
        )
//    }
}