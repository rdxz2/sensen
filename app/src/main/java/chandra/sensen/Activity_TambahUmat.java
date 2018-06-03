package chandra.sensen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Activity_TambahUmat extends AppCompatActivity {

    private EditText idumat_edit;
    private EditText nama_edit;
    private EditText alamat_edit;
    private EditText tgl_lahir_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_umat);

        cekIdUmat();

        final Calendar calendar = Calendar.getInstance();

        //TANGGAL AWAL
        tgl_lahir_edit = (EditText) findViewById(R.id.tgl_lahir_edit);
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
                new DatePickerDialog(Activity_TambahUmat.this, dateAwal, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TextView nama_alert_text = (TextView) findViewById(R.id.nama_alert_text);
        final TextView alamat_alert_text = (TextView) findViewById(R.id.alamat_alert_text);
        final TextView tgl_lahir_alert_text = (TextView) findViewById(R.id.tgl_lahir_alert_text);

        //BUTTON TAMBAH
        Button tambahButton = (Button) findViewById(R.id.tambah_button);
        tambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_alert_text.setVisibility(View.GONE);
                alamat_alert_text.setVisibility(View.GONE);
                tgl_lahir_alert_text.setVisibility(View.GONE);

                idumat_edit = (EditText) findViewById(R.id.idumat_edit);
                nama_edit = (EditText) findViewById(R.id.nama_edit);
                alamat_edit = (EditText) findViewById(R.id.alamat_edit);

                //BENER
                if(true){
                    new tambahUmat().execute(idumat_edit.getText().toString(), nama_edit.getText().toString(), tgl_lahir_edit.getText().toString(), alamat_edit.getText().toString());
                }
                else{
                    //NAMA SALAH
                    if(!((idumat_edit.getText().toString().length() >=5 ) && (idumat_edit.getText().toString().length() <= 20))){
                        nama_alert_text.setVisibility(View.VISIBLE);
                    }
                    //ALAMAT SALAH
                    if(alamat_edit.getText().toString().equals("")){
                        alamat_alert_text.setVisibility(View.VISIBLE);
                    }
                    //TANGGAL LAHIR SALAH
                    if(tgl_lahir_edit.getText().toString().equals("")){
                        tgl_lahir_alert_text.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        Button batalButton = (Button) findViewById(R.id.batal_button);
        batalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //TODO: dapetin idumat
    void cekIdUmat(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
                String jsonString = service.responseBody;
                ArrayList<HashMap<String, String>> umats = new ArrayList<>();
                try {
                    JSONArray umatArray = new JSONArray(jsonString);
                    JSONObject umatObject = umatArray.getJSONObject(umatArray.length()-1);
                    String idumat = umatObject.getString("idumat");
                    int idumat_int = Integer.parseInt(idumat.substring(2));
                    idumat_int++;
                    final String idumat_str = Integer.toString(idumat_int);
                    Activity_TambahUmat.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idumat_edit = (EditText) findViewById(R.id.idumat_edit);
                            idumat_edit.setText(idumat_str);
                        }
                    });
                    for (int i = 0; i<umatArray.length(); i++){
                    }
                }
                catch (JSONException e){e.printStackTrace();}
            }
        }).start();
    }

    //TODO: tambah umat
    class tambahUmat extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try{
                String idumat = params[0];
                String nama = params[1];
                String tgl_lahir = params[1];
                String alamat = params[1];
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
            Toast.makeText(Activity_TambahUmat.this, aBoolean ? "ya" : "ga", Toast.LENGTH_SHORT).show();
        }
    }
}
