package com.mozhimen.composek.imagek

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.RequestState
import com.bumptech.glide.integration.compose.ImageKGlide
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.RequestBuilderTransform
import com.bumptech.glide.integration.compose.Transition

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageKGlide(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    loading: Placeholder? = null,
    failure: Placeholder? = null,
    onLoading: ((RequestState.Loading) -> Unit)? = null,
    onSuccess: ((RequestState.Success) -> Unit)? = null,
    onError: ((RequestState.Failure) -> Unit)? = null,
    transition: Transition.Factory? = null,
    requestBuilderTransform: RequestBuilderTransform<Drawable> = { it },
) = ImageKGlide(
    model, contentDescription, modifier, alignment, contentScale, alpha, colorFilter, loading, failure, onLoading, onSuccess, onError, transition, requestBuilderTransform
)