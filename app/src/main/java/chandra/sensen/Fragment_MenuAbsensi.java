package chandra.sensen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Fragment_MenuAbsensi extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_MenuAbsensi() {
        // Required empty public constructor
    }

    public static Fragment_MenuAbsensi newInstance(String param1, String param2) {
        Fragment_MenuAbsensi fragment = new Fragment_MenuAbsensi();
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

    //INIT
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> tanggal_list;
    HashMap<String, List<String>> absensi_list;
    ArrayList<Contract_Umat> umat_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_absensi, container, false);
        //INIT
        final Calendar calendarAwal = Calendar.getInstance();
        final Calendar calendarAkhir = Calendar.getInstance();
        //TANGGAL AWAL
        final EditText tanggalawalEdit= (EditText) v.findViewById(R.id.tanggal_awal_edit);
        final DatePickerDialog.OnDateSetListener dateAwal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarAwal.set(Calendar.YEAR, year);
                calendarAwal.set(Calendar.MONTH, monthOfYear);
                calendarAwal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tanggalawalEdit.setText(sdf.format(calendarAwal.getTime()));
            }
        };
        tanggalawalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateAwal, calendarAwal.get(Calendar.YEAR), calendarAwal.get(Calendar.MONTH), calendarAwal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //TANGGAL AKHIR
        final EditText tanggalakhirEdit= (EditText) v.findViewById(R.id.tanggal_akhir_edit);
        final DatePickerDialog.OnDateSetListener dateAkhir = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarAkhir.set(Calendar.YEAR, year);
                calendarAkhir.set(Calendar.MONTH, monthOfYear);
                calendarAkhir.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                //KALO TANGGAL AWAL < TANGGAL AKHIR (INPUT BENER)
                if(!calendarAwal.after(calendarAkhir)){
                    tanggalakhirEdit.setText(sdf.format(calendarAkhir.getTime()));
                    //KALO TANGGAL AWAL GA KOSONG
                    if(!tanggalawalEdit.getText().toString().equals("")){
                        //CARI
                        new listingAbsensi().execute(tanggalawalEdit.getText().toString(), tanggalakhirEdit.getText().toString());
//                        tanggalawalEdit.setText("");
//                        tanggalakhirEdit.setText("");
                    }
                    else{
                        Toast.makeText(getActivity(), "Isi terlebih dahulu tanggal awal", Toast.LENGTH_SHORT).show();
                    }
                }
                //KALO TANGGAL AWAL > TANGGAL AKHIR (INPUT SALAH)
                else {
                    Toast.makeText(getActivity(), "Tanggal awal harus sebelum tanggal akhir", Toast.LENGTH_SHORT).show();
                    tanggalakhirEdit.setText("");
                }
            }
        };
        tanggalakhirEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateAkhir, calendarAkhir.get(Calendar.YEAR), calendarAkhir.get(Calendar.MONTH), calendarAkhir.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //CEK KONEKSI INTERNET
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        //FAB REFRESH ADBSENSI
        FloatingActionButton fab = getActivity().findViewById(R.id.refresh_absensi_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new listingAbsensi().execute("0000-00-00", "9999-99-99");
            }
        });
        fab.setVisibility(View.GONE);
        //KALO ADA KONEKSI INTERNET
        if(isConnected){
            fab.setVisibility(View.VISIBLE);
            expandableListView = getActivity().findViewById(R.id.absensi_expandable);
            absensi_list = new HashMap<>();
            umat_list = new ArrayList<>();
            new listingAbsensi().execute("0000-00-00", "9999-99-99");
        }
        //KALO GAADA KONEKSI INTERNET
        else{
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    class listingAbsensi extends AsyncTask<String, Void, Boolean> {
        //INIT
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TAMPILIN PROGRESS DIALOG
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Mengambil data absen");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //PARAMETER FILTER
            String tanggal_awal = params[0];
            String tanggal_akhir = params[1];
            //INIT
            umat_list.clear();
            absensi_list.clear();
            Service_WebService service = new Service_WebService("http://absenpadum.top/DataAbsensi.php","GET","");
            String jsonString = service.responseBody;
            Service_WebService service2 = new Service_WebService("http://absenpadum.top/TampilData.php", "GET", "");
            String jsonString2 = service2.responseBody;
            try {
                //INIT
                JSONArray absensiArray = new JSONArray(jsonString);
                JSONArray umatArray = new JSONArray(jsonString2);
                ArrayList<String> tanggal_nodup = new ArrayList<>(), nama_nodup = new ArrayList<>();
                String[] tanggal = new String[absensiArray.length()], nama = new String[absensiArray.length()];
                //LISTING DATA ABSENSI
                for(int a = 0; a < absensiArray.length(); a++){
                    JSONObject absensiObject = absensiArray.getJSONObject(a);
                    tanggal[a] = absensiObject.getString("Tanggal_absen");
                    nama[a] = absensiObject.getString("Nama");
                    //FILTER TANGGAL
                    if(absensiObject.getString("Tanggal_absen").compareTo(tanggal_awal) >= 0 && absensiObject.getString("Tanggal_absen").compareTo(tanggal_akhir) <= 0){
                        //GROUPING TANGGAL ABSENSI
                        if(tanggal_nodup.size() == 0 || !tanggal_nodup.contains(tanggal[a])) tanggal_nodup.add(tanggal[a]);
                        if(nama_nodup.size() == 0 || !nama_nodup.contains(nama[a])) nama_nodup.add(nama[a]);
                    }
                }
                //LISTING DATA UMAT
                for(int a = 0; a < umatArray.length(); a++){
                    JSONObject umatObject = umatArray.getJSONObject(a);
                    //GROUPING UMAT
                    if(nama_nodup.contains(umatObject.getString("Nama"))){
                        umat_list.add(new Contract_Umat(umatObject.getString("IDUmat"), umatObject.getString("Nama"), umatObject.getString("Tgl_lahir"), umatObject.getString("alamat")));
                    }
                }
                //GROUPING NAMA KE TANGGAL ABSEN
                for(int a = 0; a < tanggal_nodup.size(); a++){
                    List<String> nama_list = new ArrayList<>();
                    for(int b = 0; b < absensiArray.length(); b++){
                        if(tanggal[b].equals(tanggal_nodup.get(a))) nama_list.add(nama[b]);
                    }
                    absensi_list.put(tanggal_nodup.get(a), nama_list);
                }
            }
            catch (JSONException e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            tanggal_list = new ArrayList<String>(absensi_list.keySet());
            expandableListAdapter = new Adapter_MenuAbsensi(getContext(), tanggal_list, absensi_list);
            expandableListView.setAdapter(expandableListAdapter);
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    //DIALOG BOX
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    final View view2 = layoutInflater.inflate(R.layout.dialog_info_umat, null, false);
                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alertDialogBuilder.setView(view2);
                    final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    //INIT
                    final int pos = childPosition;
                    TextView idumat_text = view2.findViewById(R.id.idumat_text);
                    TextView nama_text = view2.findViewById(R.id.nama_text);
                    TextView alamat_text = view2.findViewById(R.id.alamat_text);
                    TextView tgl_lahir_text = view2.findViewById(R.id.tgl_lahir_text);
                    ImageView foto_image = view2.findViewById(R.id.foto_image);
                    //SET SEMUA TEXTVIEW & IMAGEVIEW
                    for(int a = 0; a< umat_list.size(); a++){
                        final Contract_Umat c = umat_list.get(a);
                        if(c.getNama().equals(absensi_list.get(tanggal_list.get(groupPosition)).get(childPosition))){
                            idumat_text.setText(c.getIdUmat());
                            nama_text.setText(c.getNama());
                            alamat_text.setText(c.getAlamat());
                            tgl_lahir_text.setText(c.getTglLahir());
//                            if(!c.getFoto().equals("")){
//
//                            }
                            //BUTTON UBAH
                            Button ubahButton = view2.findViewById(R.id.ubah_button);
                            ubahButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(view.getContext(), Activity_UbahUmat.class);
                                    i.putExtra("IDUMAT", c.getIdUmat());
                                    i.putExtra("NAMA", c.getNama());
                                    i.putExtra("TGL_LAHIR", c.getTglLahir());
                                    i.putExtra("ALAMAT", c.getAlamat());
                                    startActivity(i);
                                }
                            });
                            //BUTTON KEMBALI
                            final Button kembaliButton = view2.findViewById(R.id.kembali_button);
                            kembaliButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.hide();
                                }
                            });
                            break;
                        }
                    }
                    return false;
                }
            });
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
