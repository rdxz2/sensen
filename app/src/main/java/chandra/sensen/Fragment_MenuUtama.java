package chandra.sensen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fragment_MenuUtama extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_MenuUtama() {
        // Required empty public constructor
    }

    public static Fragment_MenuUtama newInstance(String param1, String param2) {
        Fragment_MenuUtama fragment = new Fragment_MenuUtama();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private int BARCODE_READER_REQUEST_CODE = 1;
    EditText idEdit;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BARCODE_READER_REQUEST_CODE){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = (Barcode) data.getParcelableExtra(Activity_BarcodeCapture.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    idEdit.setText(barcode.displayValue);
                }
                else{
                    idEdit.setText("Kode QR belum terdeteksi");
                }
            }
            else{
                Toast.makeText(getActivity(), "Terjadi kesalahan saat membaca kode QR", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    String id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_utama, container, false);

        idEdit = (EditText) v.findViewById(R.id.id_edit);

        //BUTTON ABSEN
        Button absenButton = v.findViewById(R.id.absen_button);
        absenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idEdit.getText().toString();
                new inputAbsen().execute(id);
            }
        });

        //BUTTON SCAN QR
        Button qrButton = v.findViewById(R.id.qr_button);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), Activity_BarcodeCapture.class), BARCODE_READER_REQUEST_CODE);
            }
        });

        return v;
    }

    class inputAbsen extends AsyncTask<String, Void, Boolean> {
        boolean berhasil = false;
        @Override
        protected Boolean doInBackground(String... params) {
            String idumat = params[0];
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("http://absenpadum.top/AbsenInput.php").openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                connection.getOutputStream().write(String.format("IDUmat=%s", idumat).getBytes());
                Log.d("DOINBACKRGOUND", "IDUMAT = " + idumat);
                InputStream input = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) stringBuilder.append(line);
                connection.disconnect();
                berhasil = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(berhasil) Toast.makeText(getActivity(), id + " berhasil diabsen", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getActivity(), "Gagal melakukan absen", Toast.LENGTH_SHORT).show();
            idEdit.setText("");
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
