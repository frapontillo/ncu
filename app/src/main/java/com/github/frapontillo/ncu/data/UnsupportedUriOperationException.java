package com.github.frapontillo.ncu.data;

import android.net.Uri;

public class UnsupportedUriOperationException extends UnsupportedOperationException {

    private final String operation;
    private final Uri uri;

    public UnsupportedUriOperationException(String operation, Uri uri) {
        this.operation = operation;
        this.uri = uri;
    }

    @Override
    public String getMessage() {
        return "Operation " + operation + " is not supported for URI " + uri.toString();
    }
}
