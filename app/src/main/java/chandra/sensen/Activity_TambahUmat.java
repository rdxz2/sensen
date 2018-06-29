package chandra.sensen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Activity_TambahUmat extends AppCompatActivity {

    //INIT
    private EditText idumat_edit;
    private EditText nama_edit;
    private EditText alamat_edit;
    private EditText tgl_lahir_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_umat);
        //ISI ID UMAT
        cekIdUmat();
        //INIT
        final Calendar calendar = Calendar.getInstance();
        //TANGGAL
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
        //SAAT TANGGAL LAHIR DIKLIK
        tgl_lahir_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Activity_TambahUmat.this, dateAwal, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //INIT
        final TextView nama_alert_text = findViewById(R.id.nama_alert_text);
        final TextView alamat_alert_text = findViewById(R.id.alamat_alert_text);
        final TextView tgl_lahir_alert_text = findViewById(R.id.tgl_lahir_alert_text);
        //BUTTON TAMBAH
        Button tambahButton = findViewById(R.id.tambah_button);
        tambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //INIT
                nama_alert_text.setVisibility(View.GONE);
                alamat_alert_text.setVisibility(View.GONE);
                tgl_lahir_alert_text.setVisibility(View.GONE);
                idumat_edit = findViewById(R.id.idumat_edit);
                nama_edit = findViewById(R.id.nama_edit);
                alamat_edit = findViewById(R.id.alamat_edit);
                boolean bener = true;
                //NAMA SALAH
                if(!((idumat_edit.getText().toString().length() >=5 ) && (idumat_edit.getText().toString().length() <= 20))){
                    nama_alert_text.setVisibility(View.VISIBLE);
                    bener = false;
                }
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
                //BENER -> MASUKIN DATA KE DB
                if(bener) {
                    new tambahUmat().execute(idumat_edit.getText().toString(), nama_edit.getText().toString(), tgl_lahir_edit.getText().toString(), alamat_edit.getText().toString());
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

    //AMBIL ID
    void cekIdUmat(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //INIT
                Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
                String jsonString = service.responseBody;
                try {
                    JSONArray umatArray = new JSONArray(jsonString);
                    //AMBIL DATA UMAT YANG PALING TERAKHIR (ID UMAT PALING BESAR)
                    JSONObject umatObject = umatArray.getJSONObject(umatArray.length()-1);
                    String idumat = umatObject.getString("IDUmat");
                    //UBAH KE INT
                    int idumat_int = Integer.parseInt(idumat.substring(2));
                    //TAMBAH 1
                    final int idumat_int2 = idumat_int + 1;
                    //UPDATE ISI DARI EDITTEXT IDUMAT
                    Activity_TambahUmat.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idumat_edit = findViewById(R.id.idumat_edit);
                            idumat_edit.setText(String.format("DD%04d", idumat_int2));
                        }
                    });
                }
                catch (JSONException e){e.printStackTrace();}
            }
        }).start();
    }

    //CLASS TAMBAH UMAT
    class tambahUmat extends AsyncTask<String, Void, Boolean> {
        //INIT
        boolean sukses = false;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TAMPILIN PROGRESS DIALOG
            progressDialog = new ProgressDialog(Activity_TambahUmat.this);
            progressDialog.setMessage("Menambahkan data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //INIT
            String idumat = params[0];
            String nama = params[1];
            String tgl_lahir = params[2];
            String alamat = params[3];
            try{
                //KONEKSI KE SERVER -> BUAT TAMBAH UMAT
                HttpURLConnection connection = (HttpURLConnection) new URL("http://absenpadum.top/InputData.php").openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                connection.getOutputStream().write(String.format("IDUmat=%s&Nama=%s&Tgl_lahir=%s&alamat=%s", idumat, nama, tgl_lahir, alamat).getBytes());
                InputStream input = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) stringBuilder.append(line);
                connection.disconnect();
                sukses = true;
                return Boolean.valueOf(stringBuilder.toString());
            } catch (IOException e) {e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            //KALO SUKSES
            if(sukses){
                Toast.makeText(Activity_TambahUmat.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            }
            //KALO GA SUKSES
            else{
                Toast.makeText(Activity_TambahUmat.this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
