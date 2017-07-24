package com.shenxy.smartapploader.utils;

/**
 * Created by David on 14-7-31.
 */
public interface OnDownloadStatusChangedListener {

    public void OnProgressChanged(long total, long current);

    public void OnError(Exception ex);

    public void OnDownloadComplete(String fullPath);

    public void OnZipFileUnCompressed(String path);
}
