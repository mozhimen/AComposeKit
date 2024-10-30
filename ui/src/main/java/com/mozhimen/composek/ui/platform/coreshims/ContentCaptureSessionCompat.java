package com.mozhimen.composek.ui.platform.coreshims;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStructure;
import android.view.autofill.AutofillId;
import android.view.contentcapture.ContentCaptureSession;

import androidx.annotation.DoNotInline;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName ContentCaptureSessionCompat
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
@RestrictTo({RestrictTo.Scope.LIBRARY})
public class ContentCaptureSessionCompat {
    private static final String KEY_VIEW_TREE_APPEARING = "TREAT_AS_VIEW_TREE_APPEARING";
    private static final String KEY_VIEW_TREE_APPEARED = "TREAT_AS_VIEW_TREE_APPEARED";
    private final Object mWrappedObj;
    private final View mView;

    @RequiresApi(29)
    @NonNull
    public static ContentCaptureSessionCompat toContentCaptureSessionCompat(@NonNull ContentCaptureSession contentCaptureSession, @NonNull View host) {
        return new ContentCaptureSessionCompat(contentCaptureSession, host);
    }

    @RequiresApi(29)
    @NonNull
    public ContentCaptureSession toContentCaptureSession() {
        return (ContentCaptureSession)this.mWrappedObj;
    }

    @RequiresApi(29)
    private ContentCaptureSessionCompat(@NonNull ContentCaptureSession contentCaptureSession, @NonNull View host) {
        this.mWrappedObj = contentCaptureSession;
        this.mView = host;
    }

    @Nullable
    public AutofillId newAutofillId(long virtualChildId) {
        return Build.VERSION.SDK_INT >= 29 ? ContentCaptureSessionCompat.Api29Impl.newAutofillId((ContentCaptureSession)this.mWrappedObj, ((AutofillIdCompat) Objects.requireNonNull(ViewCompatShims.getAutofillId(this.mView))).toAutofillId(), virtualChildId) : null;
    }

    @Nullable
    public ViewStructureCompat newVirtualViewStructure(@NonNull AutofillId parentId, long virtualId) {
        return Build.VERSION.SDK_INT >= 29 ? ViewStructureCompat.toViewStructureCompat(ContentCaptureSessionCompat.Api29Impl.newVirtualViewStructure((ContentCaptureSession)this.mWrappedObj, parentId, virtualId)) : null;
    }

    public void notifyViewsAppeared(@NonNull List<ViewStructure> appearedNodes) {
        if (Build.VERSION.SDK_INT >= 34) {
            ContentCaptureSessionCompat.Api34Impl.notifyViewsAppeared((ContentCaptureSession)this.mWrappedObj, appearedNodes);
        } else if (Build.VERSION.SDK_INT >= 29) {
            ViewStructure treeAppearing = ContentCaptureSessionCompat.Api29Impl.newViewStructure((ContentCaptureSession)this.mWrappedObj, this.mView);
            ContentCaptureSessionCompat.Api23Impl.getExtras(treeAppearing).putBoolean("TREAT_AS_VIEW_TREE_APPEARING", true);
            ContentCaptureSessionCompat.Api29Impl.notifyViewAppeared((ContentCaptureSession)this.mWrappedObj, treeAppearing);

            for(int i = 0; i < appearedNodes.size(); ++i) {
                ContentCaptureSessionCompat.Api29Impl.notifyViewAppeared((ContentCaptureSession)this.mWrappedObj, (ViewStructure)appearedNodes.get(i));
            }

            ViewStructure treeAppeared = ContentCaptureSessionCompat.Api29Impl.newViewStructure((ContentCaptureSession)this.mWrappedObj, this.mView);
            ContentCaptureSessionCompat.Api23Impl.getExtras(treeAppeared).putBoolean("TREAT_AS_VIEW_TREE_APPEARED", true);
            ContentCaptureSessionCompat.Api29Impl.notifyViewAppeared((ContentCaptureSession)this.mWrappedObj, treeAppeared);
        }

    }

    public void notifyViewsDisappeared(@NonNull long[] virtualIds) {
        if (Build.VERSION.SDK_INT >= 34) {
            ContentCaptureSessionCompat.Api29Impl.notifyViewsDisappeared((ContentCaptureSession)this.mWrappedObj, ((AutofillIdCompat)Objects.requireNonNull(ViewCompatShims.getAutofillId(this.mView))).toAutofillId(), virtualIds);
        } else if (Build.VERSION.SDK_INT >= 29) {
            ViewStructure treeAppearing = ContentCaptureSessionCompat.Api29Impl.newViewStructure((ContentCaptureSession)this.mWrappedObj, this.mView);
            ContentCaptureSessionCompat.Api23Impl.getExtras(treeAppearing).putBoolean("TREAT_AS_VIEW_TREE_APPEARING", true);
            ContentCaptureSessionCompat.Api29Impl.notifyViewAppeared((ContentCaptureSession)this.mWrappedObj, treeAppearing);
            ContentCaptureSessionCompat.Api29Impl.notifyViewsDisappeared((ContentCaptureSession)this.mWrappedObj, ((AutofillIdCompat)Objects.requireNonNull(ViewCompatShims.getAutofillId(this.mView))).toAutofillId(), virtualIds);
            ViewStructure treeAppeared = ContentCaptureSessionCompat.Api29Impl.newViewStructure((ContentCaptureSession)this.mWrappedObj, this.mView);
            ContentCaptureSessionCompat.Api23Impl.getExtras(treeAppeared).putBoolean("TREAT_AS_VIEW_TREE_APPEARED", true);
            ContentCaptureSessionCompat.Api29Impl.notifyViewAppeared((ContentCaptureSession)this.mWrappedObj, treeAppeared);
        }

    }

    public void notifyViewTextChanged(@NonNull AutofillId id, @Nullable CharSequence text) {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentCaptureSessionCompat.Api29Impl.notifyViewTextChanged((ContentCaptureSession)this.mWrappedObj, id, text);
        }

    }

    @RequiresApi(29)
    private static class Api29Impl {
        private Api29Impl() {
        }

        @DoNotInline
        static void notifyViewsDisappeared(ContentCaptureSession contentCaptureSession, AutofillId hostId, long[] virtualIds) {
            contentCaptureSession.notifyViewsDisappeared(hostId, virtualIds);
        }

        @DoNotInline
        static void notifyViewAppeared(ContentCaptureSession contentCaptureSession, ViewStructure node) {
            contentCaptureSession.notifyViewAppeared(node);
        }

        @DoNotInline
        static ViewStructure newViewStructure(ContentCaptureSession contentCaptureSession, View view) {
            return contentCaptureSession.newViewStructure(view);
        }

        @DoNotInline
        static ViewStructure newVirtualViewStructure(ContentCaptureSession contentCaptureSession, AutofillId parentId, long virtualId) {
            return contentCaptureSession.newVirtualViewStructure(parentId, virtualId);
        }

        @DoNotInline
        static AutofillId newAutofillId(ContentCaptureSession contentCaptureSession, AutofillId hostId, long virtualChildId) {
            return contentCaptureSession.newAutofillId(hostId, virtualChildId);
        }

        @DoNotInline
        public static void notifyViewTextChanged(ContentCaptureSession contentCaptureSession, AutofillId id, CharSequence charSequence) {
            contentCaptureSession.notifyViewTextChanged(id, charSequence);
        }
    }

    @RequiresApi(34)
    private static class Api34Impl {
        private Api34Impl() {
        }

        @DoNotInline
        static void notifyViewsAppeared(ContentCaptureSession contentCaptureSession, List<ViewStructure> appearedNodes) {
            contentCaptureSession.notifyViewsAppeared(appearedNodes);
        }
    }

    @RequiresApi(23)
    private static class Api23Impl {
        private Api23Impl() {
        }

        @DoNotInline
        static Bundle getExtras(ViewStructure viewStructure) {
            return viewStructure.getExtras();
        }
    }
}
