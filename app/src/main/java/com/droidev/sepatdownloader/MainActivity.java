package com.droidev.sepatdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button levantamento, sepatscanner;
    TextView link;
    DownloadManager manager;
    String filename = "";

    String levantamentoURL = "https://github.com/ils94/SEPAT_Levantamento/releases/download/release/sepat-levantamento.apk";
    String sepatscannerURL = "https://github.com/ils94/SEPAT_Scanner/releases/download/release/sepat-scanner.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        levantamento = findViewById(R.id.levantamentoDownload);

        sepatscanner = findViewById(R.id.sepatscannerDownload);

        link = findViewById(R.id.link);

        link.setMovementMethod(new LinkMovementMethod());

        levantamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filename = "sepat-levantamento.apk";

                downloader(levantamentoURL);

                levantamento.setEnabled(false);
                sepatscanner.setEnabled(false);
            }
        });

        sepatscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filename = "sepat-scanner.apk";

                downloader(sepatscannerURL);
                levantamento.setEnabled(false);
                sepatscanner.setEnabled(false);
            }
        });

    }

    public void downloader(String url) {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, filename);
        file.delete();

        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long reference = manager.enqueue(request);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

                unregisterReceiver(onComplete);

                levantamento.setEnabled(true);
                sepatscanner.setEnabled(true);
            }
        }
    };
}