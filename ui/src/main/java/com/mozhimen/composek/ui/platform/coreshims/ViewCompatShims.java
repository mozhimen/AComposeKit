package com.mozhimen.composek.ui.platform.coreshims;

import android.os.Build;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.contentcapture.ContentCaptureSession;

import androidx.annotation.DoNotInline;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;

/**
 * @ClassName ViewCompatShims
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */

@RestrictTo({RestrictTo.Scope.LIBRARY})
public class ViewCompatShims {
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_AUTO = 0;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_YES = 1;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_NO = 2;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_YES_EXCLUDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_NO_EXCLUDE_DESCENDANTS = 8;

    private ViewCompatShims() {
    }

    public static void setImportantForContentCapture(@NonNull View v, int mode) {
        if (Build.VERSION.SDK_INT >= 30) {
            ViewCompatShims.Api30Impl.setImportantForContentCapture(v, mode);
        }

    }

    @Nullable
    public static ContentCaptureSessionCompat getContentCaptureSession(@NonNull View v) {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentCaptureSession session = ViewCompatShims.Api29Impl.getContentCaptureSession(v);
            return session == null ? null : ContentCaptureSessionCompat.toContentCaptureSessionCompat(session, v);
        } else {
            return null;
        }
    }

    @Nullable
    public static AutofillIdCompat getAutofillId(@NonNull View v) {
        return Build.VERSION.SDK_INT >= 26 ? AutofillIdCompat.toAutofillIdCompat(ViewCompatShims.Api26Impl.getAutofillId(v)) : null;
    }

    @RequiresApi(30)
    private static class Api30Impl {
        private Api30Impl() {
        }

        @DoNotInline
        static void setImportantForContentCapture(View view, int mode) {
            view.setImportantForContentCapture(mode);
        }
    }

    @RequiresApi(29)
    private static class Api29Impl {
        private Api29Impl() {
        }

        @DoNotInline
        static ContentCaptureSession getContentCaptureSession(View view) {
            return view.getContentCaptureSession();
        }
    }

    @RequiresApi(26)
    static class Api26Impl {
        private Api26Impl() {
        }

        @DoNotInline
        public static AutofillId getAutofillId(View view) {
            return view.getAutofillId();
        }
    }
}