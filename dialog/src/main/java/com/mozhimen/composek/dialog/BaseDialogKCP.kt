package com.mozhimen.composek.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.mozhimen.composek.elems.commons.ICompose_Listener
import com.mozhimen.xmlk.dialogk.bases.BaseDialogK
import com.mozhimen.xmlk.R

/**
 * @ClassName BaseDialogKVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 17:12
 * @Version 1.0
 */
abstract class BaseDialogKCP constructor(context: Context, @StyleRes intResTheme: Int = R.style.ThemeK_Dialog_Blur) : BaseDialogK(context, intResTheme) {

    override fun onCreateView(inflater: LayoutInflater): View? {
        return ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent(getContent())
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    abstract fun getContent(): ICompose_Listener
}