package chandra.sensen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    //ASDASDASD
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> tanggal_list;
    HashMap<String, List<String>> absensi_list;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_absensi, container, false);

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
                if(!calendarAwal.after(calendarAkhir)){
                    tanggalakhirEdit.setText(sdf.format(calendarAkhir.getTime()));
                    if(!tanggalawalEdit.getText().toString().equals("")){
                        //CARI
                        //TODO: searching absen sesuai range tanggal
                        Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Isi terlebih dahulu data-data di atas", Toast.LENGTH_SHORT).show();
                    }
                }
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

        //KALO ADA KONEKSI INTERNET
        if(isConnected){
            expandableListView = (ExpandableListView) getActivity().findViewById(R.id.absensi_expandable);
            absensi_list = DataPump_MenuAbsensi.getData();
            new listingAbsensi().execute();
        }
        //KALO GAADA KONEKSI INTERNET
        else{
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    class listingAbsensi extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Mengambil data absen");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Service_WebService service = new Service_WebService("http://absenpadum.top/DataAbsensi.php","GET","");
            String jsonString = service.responseBody;
            try {
                JSONArray absensiArray = new JSONArray(jsonString);
                for (int i = 0; i<absensiArray.length(); i++){
                    JSONObject absensiObject = absensiArray.getJSONObject(i);
                    absensi_list.add(new Contract_Absensi(absensiObject.getString("IDAbsen"), absensiObject.getString("Tanggal_absen"), absensiObject.getString("IDUmat"), absensiObject.getString("Nama")));
                }
            }
            catch (JSONException e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.hide();

            tanggal_list = new ArrayList<String>(absensi_list.keySet());
            expandableListAdapter = new Adapter_MenuAbsensi(getContext(), tanggal_list, absensi_list);
            expandableListView.setAdapter(expandableListAdapter);
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(getActivity(), tanggal_list.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(getActivity(), tanggal_list.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();

                }
            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(getActivity(), tanggal_list.get(groupPosition) + " -> " + absensi_list.get(tanggal_list.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
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
