package com.droidev.sepatdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button sepatLevantamento, sepatScanner, sepatInventario, sepatAgenda;
    TextView link;
    private Downloads downloads;

    private final String sepatLevantamentoURL = "https://github.com/ils94/SEPAT_Levantamento/releases/download/release/sepat-levantamento.apk";
    private final String sepatScannerURL = "https://github.com/ils94/SEPAT_Scanner/releases/download/release/sepat-scanner.apk";
    private final String sepatAgendaURL = "https://github.com/ils94/SEPAT_Agenda/releases/download/release/sepat-agenda.apk";
    private final String sepatInventarioURL = "https://github.com/ils94/SEPAT_Inventario/releases/download/release/sepat-inventario.apk";

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

    private void downloadIniciado() {

        Toast.makeText(MainActivity.this, "Download iniciado, aguarde.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.limparDownloads) {

            clearData();

        }
        return super.onOptionsItemSelected(item);
    }

    private void clearData() {

        try {
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
            } else {

                Runtime.getRuntime().exec("pm clear " + getApplicationContext().getPackageName());
            }

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }
}