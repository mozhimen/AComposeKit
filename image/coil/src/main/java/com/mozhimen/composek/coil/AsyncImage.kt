package com.mozhimen.composek.coil

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.Companion.DefaultTransform
import coil.compose.AsyncImagePainter.State
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate
import coil.request.CachePolicy
import coil.request.ImageRequest

/**
 * @ClassName AsyncIMage
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/18
 * @Version 1.0
 */
@Composable
fun CoilLoadImage(
    model: Any,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    placeholder: Painter? = ColorPainter(Color.Transparent),
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).respectCacheHeaders(false).build()
    val request = ImageRequest.Builder(LocalContext.current)
        .data(model)
        .allowHardware(false)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)//开启磁盘缓存策略
        .build()
    var reLoading by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (reLoading) {
            AsyncImageProgress(
                progress = -1f,
            )
        }
        CoilImage(
            model = request,
            contentDescription = contentDescription,
            imageLoader = imageLoader,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            placeholder = placeholder,
            error = error,
            fallback = fallback,
            onLoading = {
                reLoading = true
                onLoading?.invoke(it)
            },
            onSuccess = {
                reLoading = false
                onSuccess?.invoke(it)
            },
            onError = {
                reLoading = false
                onError?.invoke(it)
            },
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
            clipToBounds = clipToBounds,
            modelEqualityDelegate = modelEqualityDelegate
        )
    }
}

@Composable
fun CoilBaseImage(
    model: Any,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    placeholder: Painter = ColorPainter(Color.Transparent),
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).respectCacheHeaders(false).build()
    val request = ImageRequest.Builder(LocalContext.current)
        .data(model)
        .allowHardware(false)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)//开启磁盘缓存策略
        .build()
    CoilImage(
        model = request,
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        placeholder = placeholder,
        error = error,
        fallback = fallback,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        clipToBounds = clipToBounds,
        modelEqualityDelegate = modelEqualityDelegate
    )
}

@Composable
@NonRestartableComposable
fun CoilImage(
    model: Any?,
    contentDescription: String?,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
) {
    AsyncImage(
        model,
        contentDescription,
        imageLoader,
        modifier,
        placeholder,
        error,
        fallback,
        onLoading,
        onSuccess,
        onError,
        alignment,
        contentScale,
        alpha,
        colorFilter,
        filterQuality,
        clipToBounds,
        modelEqualityDelegate
    )
}

@Composable
@NonRestartableComposable
fun CoilImage(
    model: Any?,
    contentDescription: String?,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    transform: (State) -> State = DefaultTransform,
    onState: ((State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
) {
    AsyncImage(
        model,
        contentDescription,
        imageLoader,
        modifier,
        transform,
        onState,
        alignment,
        contentScale,
        alpha,
        colorFilter,
        filterQuality,
        clipToBounds,
        modelEqualityDelegate
    )
}

@Composable
fun AsyncImageProgress(
    progress: Float = 0.5f,
    modifier: Modifier = Modifier.size(24.dp),
    color: Color = Color.White,
    strokeWidth: Dp = 2.dp,
    backgroundColor: Color = Color.Transparent,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
//下载按钮图片
    if (progress < 0f) {
        CircularProgressIndicator(
            modifier = modifier,
            color = color,
            strokeWidth = strokeWidth,
            backgroundColor = backgroundColor,
            strokeCap = strokeCap,
        )
    } else {
        CircularProgressIndicator(
            progress = progress,
            modifier = modifier,
            color = color,
            strokeWidth = strokeWidth,
            backgroundColor = backgroundColor,
            strokeCap = strokeCap
        )
    }
}