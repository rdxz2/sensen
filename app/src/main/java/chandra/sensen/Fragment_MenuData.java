package chandra.sensen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_MenuData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_MenuData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_MenuData extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

        //FAB
        FloatingActionButton fab = v.findViewById(R.id.tambah_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_TambahUmat.class));
            }
        });

        //DAPETIN DATA
//        new ListUmat().execute();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //CARD
        new listingUmat().execute();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.dataumat_recycler);
    }

    class listingUmat extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
            String jsonString = service.responseBody;
            ArrayList<HashMap<String, String>> umats = new ArrayList<>();
            try {
                JSONArray umatArray = new JSONArray(jsonString);
                for (int i = 0; i<umatArray.length(); i++){
                    JSONObject umatObject = umatArray.getJSONObject(i);
                    umat_list.add(new Contract_Umat(umatObject.getString("idumat"), umatObject.getString("nama"), umatObject.getString("tgl_lahir"), umatObject.getString("alamat")));
//                    umat_list.add(new Contract_Umat(umatObject.getString("idumat"), umatObject.getString("nama")));
                }
            }
            catch (JSONException e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            adapter_menuData = new Adapter_MenuData(umat_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter_menuData);
        }
    }

//    class ListUmat extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
//            Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
//            String jsonString = service.responseBody;
//            ArrayList<HashMap<String, String>> umats = new ArrayList<>();
//            try {
//                JSONArray umatArray = new JSONArray(jsonString);
//                for (int i = 0; i<umatArray.length(); i++){
//                    JSONObject umatObject = umatArray.getJSONObject(i);
//                    String idumat = umatObject.getString("idumat");
//                    String nama = umatObject.getString("nama");
//                    HashMap<String, String> umat = new HashMap<>();
//                    umat.put("idumat", idumat);
//                    umat.put("nama", nama);
//                    umats.add(umat);
//                }
//            }
//            catch (JSONException e){e.printStackTrace();}
//            return umats;
//        }
//
//        ListView umatList;
//
//        @Override
//        protected void onPostExecute(ArrayList<HashMap<String, String>> umats) {
//            super.onPostExecute(umats);
//            umatList = (ListView) Fragment_MenuData.this.getView().findViewById(R.id.data_list);
//            umatList.setAdapter(new SimpleAdapter(
//                getActivity(),
//                umats,
//                android.R.layout.simple_list_item_2,
//                new String[]{"idumat", "nama"},
//                new int[]{android.R.id.text1, android.R.id.text2,}
//            ));
//        }
//    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
