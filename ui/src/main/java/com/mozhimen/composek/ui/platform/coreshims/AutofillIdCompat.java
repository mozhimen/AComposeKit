package com.mozhimen.composek.ui.platform.coreshims;

import android.view.autofill.AutofillId;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;

/**
 * @ClassName AutofillIdCompat
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
@RestrictTo({RestrictTo.Scope.LIBRARY})
public class AutofillIdCompat {
    private final Object mWrappedObj;

    @RequiresApi(26)
    private AutofillIdCompat(@NonNull AutofillId obj) {
        this.mWrappedObj = obj;
    }

    @RequiresApi(26)
    @NonNull
    public static AutofillIdCompat toAutofillIdCompat(@NonNull AutofillId autofillId) {
        return new AutofillIdCompat(autofillId);
    }

    @RequiresApi(26)
    @NonNull
    public AutofillId toAutofillId() {
        return (AutofillId)this.mWrappedObj;
    }
}
