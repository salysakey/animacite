package org.anima.model;

import android.net.Uri;

/**
 * Created by BeugFallou on 29/10/2016.
 */
public class UploadListenerEvent {
    private final Uri fullSizeUrl;

    public UploadListenerEvent(Uri fullSizeUrl) {
        this.fullSizeUrl = fullSizeUrl;
    }

    public Uri getFullSizeUrl() {
        return fullSizeUrl;
    }
}
