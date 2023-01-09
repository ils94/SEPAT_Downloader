package com.droidev.sepatdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button sepatLevantamento, sepatScanner, sepatInventario, sepatAgenda;
    TextView link;
    Downloads downloads;

    String sepatLevantamentoURL = "https://github.com/ils94/SEPAT_Levantamento/releases/download/release/sepat-levantamento.apk";
    String sepatScannerURL = "https://github.com/ils94/SEPAT_Scanner/releases/download/release/sepat-scanner.apk";
    String sepatAgendaURL = "https://github.com/ils94/SEPAT_Agenda/releases/download/release/sepat-agenda.apk";
    String sepatInventarioURL = "https://github.com/ils94/SEPAT_Inventario/releases/download/release/sepat-inventario.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloads = new Downloads();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        sepatLevantamento = findViewById(R.id.sepatLevantamentoDownload);

        sepatScanner = findViewById(R.id.sepatScannerDownload);

        sepatInventario = findViewById(R.id.sepatInventarioDownload);

        sepatAgenda = findViewById(R.id.sepatAgendaDownload);

        link = findViewById(R.id.link);

        link.setMovementMethod(new LinkMovementMethod());

        sepatLevantamento.setOnClickListener(view -> {

            long timestamp = System.currentTimeMillis();

            String filename = "sepat-levantamento" + timestamp + ".apk";

            downloadIniciado();

            downloads.downloadAndInstallApk(MainActivity.this, sepatLevantamentoURL, filename);

        });

        sepatScanner.setOnClickListener(view -> {

            long timestamp = System.currentTimeMillis();

            String filename = "sepat-scanner" + timestamp + ".apk";

            downloadIniciado();

            downloads.downloadAndInstallApk(MainActivity.this, sepatScannerURL, filename);

        });

        sepatInventario.setOnClickListener(view -> {

            long timestamp = System.currentTimeMillis();

            String filename = "sepat-inventario" + timestamp + ".apk";

            downloadIniciado();

            downloads.downloadAndInstallApk(MainActivity.this, sepatInventarioURL, filename);

        });

        sepatAgenda.setOnClickListener(view -> {

            long timestamp = System.currentTimeMillis();

            String filename = "sepat-agenda" + timestamp + ".apk";

            downloadIniciado();

            downloads.downloadAndInstallApk(MainActivity.this, sepatAgendaURL, filename);

        });

    }

    public void downloadIniciado() {

        Toast.makeText(MainActivity.this, "Download iniciado, aguarde.", Toast.LENGTH_SHORT).show();
    }
}