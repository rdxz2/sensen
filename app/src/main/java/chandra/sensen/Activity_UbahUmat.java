package chandra.sensen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Activity_UbahUmat extends AppCompatActivity {

    private EditText idumat_edit;
    private EditText nama_edit;
    private EditText alamat_edit;
    private EditText tgl_lahir_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_umat);

        //INIT
        idumat_edit = findViewById(R.id.idumat_edit);
        nama_edit = findViewById(R.id.nama_edit);
        alamat_edit = findViewById(R.id.alamat_edit);
        final TextView alamat_alert_text = findViewById(R.id.alamat_alert_text);
        final TextView tgl_lahir_alert_text = findViewById(R.id.tgl_lahir_alert_text);

        //CEK ID UMAT
//        cekIdUmat();

        final Calendar calendar = Calendar.getInstance();

        //TANGGAL AWAL
        tgl_lahir_edit = findViewById(R.id.tgl_lahir_edit);
        final DatePickerDialog.OnDateSetListener dateAwal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tgl_lahir_edit.setText(sdf.format(calendar.getTime()));
            }
        };
        tgl_lahir_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Activity_UbahUmat.this, dateAwal, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //SET & TAMPILIN DATA UMAT YANG SEKARANG
        idumat_edit.setText(getIntent().getStringExtra("IDUMAT"));
        nama_edit.setText(getIntent().getStringExtra("NAMA"));
        alamat_edit.setText(getIntent().getStringExtra("ALAMAT"));
        tgl_lahir_edit.setText(getIntent().getStringExtra("TGL_LAHIR"));

        //BUTTON UBAH
        Button ubahButton = findViewById(R.id.ubah_button);
        ubahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alamat_alert_text.setVisibility(View.GONE);
                tgl_lahir_alert_text.setVisibility(View.GONE);

                boolean bener = true;

                //ALAMAT SALAH
                if(alamat_edit.getText().toString().equals("")){
                    alamat_alert_text.setVisibility(View.VISIBLE);
                    bener = false;
                }
                //TANGGAL LAHIR SALAH
                if(tgl_lahir_edit.getText().toString().equals("")){
                    tgl_lahir_alert_text.setVisibility(View.VISIBLE);
                    bener = false;
                }
                //BENER
                if(bener) {
                    //MASUKIN DATA KE DB
                    new ubahUmat().execute(idumat_edit.getText().toString(), nama_edit.getText().toString(), tgl_lahir_edit.getText().toString(), alamat_edit.getText().toString());
                    finish();
                }
            }
        });

        //BUTTON BATAL
        Button batalButton = findViewById(R.id.batal_button);
        batalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //TODO: ubah umat
    class ubahUmat extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Activity_UbahUmat.this);
            progressDialog.setMessage("Menambahkan data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                String idumat = params[0];
                String nama = params[1];
                String tgl_lahir = params[1];
                String alamat = params[1];
                HttpURLConnection connection = (HttpURLConnection) new URL("http://absenpadum.top/UpdateData.php").openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                connection.getOutputStream().write(String.format("IDUmat=%s&Nama=%s&Tgl_lahir=%s&alamat=%s", idumat, nama, tgl_lahir, alamat).getBytes());
                InputStream input = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) stringBuilder.append(line);
                connection.disconnect();
                return Boolean.valueOf(stringBuilder.toString());
            } catch (IOException e) {e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            progressDialog.hide();

            if(aBoolean){
                Toast.makeText(Activity_UbahUmat.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(Activity_UbahUmat.this, "Data gagal diubah", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
