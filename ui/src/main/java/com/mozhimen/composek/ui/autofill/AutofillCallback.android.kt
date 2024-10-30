package com.mozhimen.composek.ui.autofill

import android.os.Build
import android.util.Log
import android.view.View
import android.view.autofill.AutofillManager
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.ui.ExperimentalComposeUiApi

/**
 * @ClassName AutofillCallback
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Autofill Manager callback.
 *
 * This callback is called when we receive autofill events. It adds some logs that can be useful
 * for debug purposes.
 */
@RequiresApi(Build.VERSION_CODES.O)
internal object AutofillCallback : AutofillManager.AutofillCallback() {
    override fun onAutofillEvent(view: View, virtualId: Int, event: Int) {
        super.onAutofillEvent(view, virtualId, event)
        Log.d(
            "Autofill Status",
            when (event) {
                EVENT_INPUT_SHOWN -> "Autofill popup was shown."
                EVENT_INPUT_HIDDEN -> "Autofill popup was hidden."
                EVENT_INPUT_UNAVAILABLE -> """
                        |Autofill popup isn't shown because autofill is not available.
                        |
                        |Did you set up autofill?
                        |1. Go to Settings > System > Languages&input > Advanced > Autofill Service
                        |2. Pick a service
                        |
                        |Did you add an account?
                        |1. Go to Settings > System > Languages&input > Advanced
                        |2. Click on the settings icon next to the Autofill Service
                        |3. Add your account
                        """.trimMargin()
                else -> "Unknown status event."
            }
        )
    }

    /**
     * Registers the autofill debug callback.
     */
    @ExperimentalComposeUiApi
    @DoNotInline
    fun register(autofill: AndroidAutofill) {
        autofill.autofillManager.registerCallback(this)
    }

    /**
     * Unregisters the autofill debug callback.
     */
    @ExperimentalComposeUiApi
    @DoNotInline
    fun unregister(autofill: AndroidAutofill) {
        autofill.autofillManager.unregisterCallback(this)
    }
}