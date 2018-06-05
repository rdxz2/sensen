package chandra.sensen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_MenuData extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_MenuData() {
        // Required empty public constructor
    }

    public static Fragment_MenuData newInstance(String param1, String param2) {
        Fragment_MenuData fragment = new Fragment_MenuData();
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

    private RecyclerView recyclerView;
    private Adapter_MenuData adapter_menuData;
    private ArrayList<Contract_Umat> umat_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_data, container, false);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //CEK KOONEKSI INTERNET
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //FAB
        FloatingActionButton fab = getActivity().findViewById(R.id.tambah_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_TambahUmat.class));
            }
        });
        fab.setVisibility(View.GONE);

        //KALO ADA KONEKSI INTERNET
        if(isConnected){
            fab.setVisibility(View.VISIBLE);
            new listingUmat().execute();
            recyclerView = getActivity().findViewById(R.id.dataumat_recycler);
        }
        //KALO GAADA KONEKSI INTERNET
        else{
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }

    }

    class listingUmat extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
            String jsonString = service.responseBody;
            try {
                JSONArray umatArray = new JSONArray(jsonString);
                for (int i = 0; i<umatArray.length(); i++){
                    JSONObject umatObject = umatArray.getJSONObject(i);
                    umat_list.add(new Contract_Umat(umatObject.getString("IDUmat"), umatObject.getString("Nama"), umatObject.getString("Tgl_lahir"), umatObject.getString("alamat")));
//                    umat_list.add(new Contract_Umat(umatObject.getString("IDUmat"), umatObject.getString("Nama"), umatObject.getString("Tgl_lahir"), umatObject.getString("alamat"), umatObject.getString("foto")));
//                    Log.d("UMAT LIST", umat_list.get(i).getIdUmat());
                }
            }
            catch (JSONException e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            adapter_menuData = new Adapter_MenuData(umat_list, getContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter_menuData);
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
        } else throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");

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
