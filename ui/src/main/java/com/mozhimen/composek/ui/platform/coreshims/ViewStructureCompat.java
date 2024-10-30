package com.mozhimen.composek.ui.platform.coreshims;

import android.os.Build;
import android.view.ViewStructure;

import androidx.annotation.DoNotInline;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;

/**
 * @ClassName ViewStructureCompat
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
@RestrictTo({RestrictTo.Scope.LIBRARY})
public class ViewStructureCompat {
    private final Object mWrappedObj;

    @RequiresApi(23)
    @NonNull
    public static ViewStructureCompat toViewStructureCompat(@NonNull ViewStructure contentCaptureSession) {
        return new ViewStructureCompat(contentCaptureSession);
    }

    @RequiresApi(23)
    @NonNull
    public ViewStructure toViewStructure() {
        return (ViewStructure)this.mWrappedObj;
    }

    private ViewStructureCompat(@NonNull ViewStructure viewStructure) {
        this.mWrappedObj = viewStructure;
    }

    public void setText(@NonNull CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 23) {
            ViewStructureCompat.Api23Impl.setText((ViewStructure)this.mWrappedObj, charSequence);
        }

    }

    public void setClassName(@NonNull String string) {
        if (Build.VERSION.SDK_INT >= 23) {
            ViewStructureCompat.Api23Impl.setClassName((ViewStructure)this.mWrappedObj, string);
        }

    }

    public void setTextStyle(float size, int fgColor, int bgColor, int style) {
        if (Build.VERSION.SDK_INT >= 23) {
            ViewStructureCompat.Api23Impl.setTextStyle((ViewStructure)this.mWrappedObj, size, fgColor, bgColor, style);
        }

    }

    public void setContentDescription(@NonNull CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 23) {
            ViewStructureCompat.Api23Impl.setContentDescription((ViewStructure)this.mWrappedObj, charSequence);
        }

    }

    public void setDimens(int left, int top, int scrollX, int scrollY, int width, int height) {
        if (Build.VERSION.SDK_INT >= 23) {
            ViewStructureCompat.Api23Impl.setDimens((ViewStructure)this.mWrappedObj, left, top, scrollX, scrollY, width, height);
        }

    }

    @RequiresApi(23)
    private static class Api23Impl {
        private Api23Impl() {
        }

        @DoNotInline
        static void setDimens(ViewStructure viewStructure, int left, int top, int scrollX, int scrollY, int width, int height) {
            viewStructure.setDimens(left, top, scrollX, scrollY, width, height);
        }

        @DoNotInline
        static void setText(ViewStructure viewStructure, CharSequence charSequence) {
            viewStructure.setText(charSequence);
        }

        @DoNotInline
        static void setClassName(ViewStructure viewStructure, String string) {
            viewStructure.setClassName(string);
        }

        @DoNotInline
        static void setContentDescription(ViewStructure viewStructure, CharSequence charSequence) {
            viewStructure.setContentDescription(charSequence);
        }

        @DoNotInline
        static void setTextStyle(ViewStructure viewStructure, float size, int fgColor, int bgColor, int style) {
            viewStructure.setTextStyle(size, fgColor, bgColor, style);
        }
    }
}
