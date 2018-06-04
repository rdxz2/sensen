package chandra.sensen;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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

        idumat_edit = findViewById(R.id.idumat_edit);
        nama_edit = findViewById(R.id.nama_edit);
        alamat_edit = findViewById(R.id.alamat_edit);
        final TextView alamat_alert_text = findViewById(R.id.alamat_alert_text);
        final TextView tgl_lahir_alert_text = findViewById(R.id.tgl_lahir_alert_text);

        cekIdUmat();

        final Calendar calendar = Calendar.getInstance();

        //TANGGAL AWAL
        tgl_lahir_edit = findViewById(R.id.tgl_lahir_edit);
        final DatePickerDialog.OnDateSetListener dateAwal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
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
                }
            }
        });

        Button batalButton = findViewById(R.id.batal_button);
        batalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void cekIdUmat(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
                String jsonString = service.responseBody;
                try {
                    JSONArray umatArray = new JSONArray(jsonString);
                    JSONObject umatObject = umatArray.getJSONObject(umatArray.length()-1);
                    String idumat = umatObject.getString("idumat");
                    int idumat_int = Integer.parseInt(idumat.substring(2));
                    idumat_int++;
                    final String idumat_str = Integer.toString(idumat_int);
                    Activity_UbahUmat.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idumat_edit = findViewById(R.id.idumat_edit);
                            idumat_edit.setText(idumat_str);
                        }
                    });
                }
                catch (JSONException e){e.printStackTrace();}
            }
        }).start();
    }

    //TODO: ubah umat
    static class ubahUmat extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try{
                String idumat = params[0];
                String nama = params[1];
                String tgl_lahir = params[1];
                String alamat = params[1];
                //TODO: linknya
                HttpURLConnection connection = (HttpURLConnection) new URL("http://absenpadum.top/InputData.php").openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                connection.getOutputStream().write(String.format("idumat=%s&nama=%s&tgl_lahir=%s&alamat=%s", idumat, nama, tgl_lahir, alamat).getBytes());
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
        }
    }
}
