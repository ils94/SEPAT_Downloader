package com.droidev.sepatdownloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.sepatLevantamentoDownload);
        Button button2 = findViewById(R.id.sepatScannerDownload);
        Button button3 = findViewById(R.id.sepatAgendaDownload);
        Button button4 = findViewById(R.id.sepatInventarioDownload);

        button1.setOnClickListener(v -> {
            String downloadUrl = "https://github.com/ils94/SEPAT_Levantamento/releases/download/release/sepat-levantamento.apk";
            downloadAndInstallApk(downloadUrl);
        });

        button2.setOnClickListener(v -> {
            String downloadUrl = "https://github.com/ils94/SEPAT_Scanner/releases/download/release/sepat-scanner.apk";
            downloadAndInstallApk(downloadUrl);
        });

        button3.setOnClickListener(v -> {
            String downloadUrl = "https://github.com/ils94/SEPAT_Agenda/releases/download/release/sepat-agenda.apk";
            downloadAndInstallApk(downloadUrl);
        });

        button4.setOnClickListener(v -> {
            String downloadUrl = "https://github.com/ils94/SEPAT_Inventario/releases/download/release/sepat-inventario.apk";
            downloadAndInstallApk(downloadUrl);
        });
    }

    private void downloadAndInstallApk(String downloadUrl) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            new DownloadTask().execute(downloadUrl);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String downloadUrl = "https://example.com/apk1.apk";
            downloadAndInstallApk(downloadUrl);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Downloading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String downloadUrl = params[0];
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Disable caching
                connection.setUseCaches(false);
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Pragma", "no-cache");

                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                int fileLength = connection.getContentLength();

                InputStream input = connection.getInputStream();
                File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss", Locale.getDefault()).format(new Date());
                File outputFile = new File(outputDir, "app_" + timestamp + ".apk");
                FileOutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return outputFile.getAbsolutePath();
            } catch (Exception e) {
                return null;
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String filePath) {
            super.onPostExecute(filePath);
            progressDialog.dismiss();
            if (filePath != null) {
                File apkFile = new File(filePath);
                Uri apkUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", apkFile);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }
}
