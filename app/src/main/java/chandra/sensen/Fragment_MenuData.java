package chandra.sensen;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_MenuData.
     */
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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_data, container, false);

        //FAB
        FloatingActionButton fab = v.findViewById(R.id.tambah_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_TambahUmat.class));
            }
        });

        new ListUmat().execute();

        return v;
    }

    ListView umatList;

    class ListUmat extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
            String jsonString = service.responseBody;
            ArrayList<HashMap<String, String>> umats = new ArrayList<>();
            try {
                JSONArray umatArray = new JSONArray(jsonString);
                for (int i = 0; i<umatArray.length(); i++){
                    JSONObject umatObject = umatArray.getJSONObject(i);
                    //TODO: biodata yang lain
                    String idumat = umatObject.getString("idumat");
                    String nama = umatObject.getString("nama");
                    HashMap<String, String> umat = new HashMap<>();
                    umat.put("idumat", idumat);
                    umat.put("nama", nama);
                    umats.add(umat);
                }
            }
            catch (JSONException e){e.printStackTrace();}
            return umats;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> umats) {
            super.onPostExecute(umats);
            umatList = (ListView) Fragment_MenuData.this.getView().findViewById(R.id.data_list);
            umatList.setAdapter(new SimpleAdapter(
                getActivity(),
                umats,
                android.R.layout.simple_list_item_2,
                new String[]{"idumat", "nama"},
                new int[]{android.R.id.text1, android.R.id.text2,}
            ));
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
