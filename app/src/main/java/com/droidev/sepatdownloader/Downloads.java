package com.droidev.sepatdownloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class Downloads {

    private BroadcastReceiver downloadReceiver;

    public void downloadAndInstallApk(Context context, String url, String filename) {

        // Download the APK file

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(new DownloadManager.Request(Uri.parse(url))
                .setMimeType("application/vnd.android.package-archive")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename));

        // Register a broadcast receiver to listen for the DOWNLOAD_COMPLETE intent
        class DownloadCompleteReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (receivedDownloadId == downloadId) {
                    // Check if the download was successful
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            // Download was successful, install the APK file

                            Toast.makeText(context, "Download finalizado.", Toast.LENGTH_SHORT).show();

                            context.unregisterReceiver(downloadReceiver);

                            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
                            Uri fileUri = FileProvider.getUriForFile(context, "com.example.fileprovider", file);

                            Intent installIntent = new Intent(Intent.ACTION_VIEW);
                            installIntent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            context.startActivity(installIntent);

                        }
                    }
                }
            }
        }

        downloadReceiver = new DownloadCompleteReceiver();
        IntentFilter downloadFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, downloadFilter);
    }
}
