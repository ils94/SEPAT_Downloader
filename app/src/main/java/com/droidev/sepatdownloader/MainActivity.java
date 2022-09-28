package com.droidev.sepatdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button sepatLevantamento, sepatScanner, sepatInventario, sepatAgenda;
    TextView link;
    DownloadManager manager;
    String filename = "";

    String sepatDownloaderURL = "https://github.com/ils94/SEPAT_Downloader/releases/download/release/sepat-downloader.apk";
    String sepatLevantamentoURL = "https://github.com/ils94/SEPAT_Levantamento/releases/download/release/sepat-levantamento.apk";
    String sepatScannerURL = "https://github.com/ils94/SEPAT_Scanner/releases/download/release/sepat-scanner.apk";
    String sepatAgendaURL = "https://github.com/ils94/SEPAT_Agenda/releases/download/release/sepat-agenda.apk";
    String sepatInventarioURL = "https://github.com/ils94/SEPAT_Inventario/releases/download/release/sepat-inventario.apk";

    public void downloader(String url) {

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

                enableDisableButtons();
            }
        }
    };

    public void enableDisableButtons() {

        if (sepatLevantamento.isEnabled()) {

            sepatLevantamento.setEnabled(false);
            sepatScanner.setEnabled(false);
            sepatAgenda.setEnabled(false);
            sepatInventario.setEnabled(false);
        } else {

            sepatLevantamento.setEnabled(true);
            sepatScanner.setEnabled(true);
            sepatAgenda.setEnabled(true);
            sepatInventario.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        sepatLevantamento = findViewById(R.id.sepatLevantamentoDownload);

        sepatScanner = findViewById(R.id.sepatScannerDownload);

        sepatInventario = findViewById(R.id.sepatInventarioDownload);

        sepatAgenda = findViewById(R.id.sepatAgendaDownload);

        link = findViewById(R.id.link);

        link.setMovementMethod(new LinkMovementMethod());

        sepatLevantamento.setOnClickListener(view -> {

            filename = "sepat-levantamento.apk";

            downloader(sepatLevantamentoURL);

            enableDisableButtons();
        });

        sepatScanner.setOnClickListener(view -> {

            filename = "sepat-scanner.apk";

            downloader(sepatScannerURL);

            enableDisableButtons();
        });

        sepatInventario.setOnClickListener(view -> {

            filename = "sepat-inventario.apk";

            downloader(sepatInventarioURL);

            enableDisableButtons();
        });

        sepatAgenda.setOnClickListener(view -> {

            filename = "sepat-agenda.apk";

            downloader(sepatAgendaURL);

            enableDisableButtons();
        });

    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.atualizar) {
            filename = "sepat-downloader.apk";

            downloader(sepatDownloaderURL);

            enableDisableButtons();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}